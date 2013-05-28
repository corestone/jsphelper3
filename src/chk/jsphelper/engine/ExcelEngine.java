package chk.jsphelper.engine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Boolean;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import chk.jsphelper.Constant;
import chk.jsphelper.DataList;
import chk.jsphelper.Parameter;
import chk.jsphelper.ServiceCaller;
import chk.jsphelper.module.mapper.ParameterMapper;
import chk.jsphelper.object.Excel;
import chk.jsphelper.util.StringUtil;
import chk.jsphelper.value.TransactionValue;

/**
 * @author Corestone
 */
public class ExcelEngine implements InterfaceEngine
{
	private int applyCount = 0;
	private final List<DataList> dbData = new ArrayList<DataList>();
	private final Excel excel;
	private Parameter excelParam;
	private final Parameter param;
	private DataList rsData;

	/**
	 * @param object
	 * @param param
	 */
	public ExcelEngine (final Excel object, final Parameter param)
	{
		this.excel = object;
		this.param = param;
	}

	public void execute () throws Exception
	{
		try
		{
			final ParameterMapper paramMapper = new ParameterMapper(this.param);
			paramMapper.checkParam();

			final String filePath = paramMapper.convertMappingText(this.excel.getPath());
			final String jdbcID = paramMapper.convertMappingText(this.excel.getTransaction());

			if (!(this.excel.getSourceEnum().equals("EXCEL") || this.excel.getTargetEnum().equals("EXCEL")))
			{
				throw new Exception("EXCEL 오브젝트는 반드시 source나 target의 어느 한쪽에 'excel'이 있어야 합니다.");
			}

			switch (this.excel.getSourceEnum())
			{
				case EXCEL :
					this.readExcel(filePath, this.excel.getStart());
					break;
				case DB :
					this.readDB(jdbcID);
					break;
				default :
					throw new Exception("EXCEL 오브젝트의 source는 'db' 과 'excel'만을 써야 합니다.");
			}

			switch (this.excel.getTargetEnum())
			{
				case EXCEL :
					this.writeExcel(filePath);
					break;
				case DB :
					this.writeDB(jdbcID);
					break;
				case DATALIST :
					this.writeRecordSet();
					break;
				default :
					throw new Exception("EXCEL 오브젝트의 target은 'db' 과 'excel', 'recordset'만을 써야 합니다.");
			}
		}
		catch (final Exception e)
		{
			throw e;
		}
	}

	public Map<String, Object> getValueObject ()
	{
		final Map<String, Object> hm = new HashMap<String, Object>();
		hm.put("applyCount", this.applyCount);
		hm.put("DataList", this.rsData);
		return hm;
	}

	private void readDB (final String jdbcID) throws Exception
	{
		final ServiceCaller pf = new ServiceCaller();
		final TransactionValue transaction = pf.executeTransaction(jdbcID, this.param);
		if (!transaction.isSuccess())
		{
			throw new Exception("[" + this.excel.getId() + "]에서 데이타베이스를 읽어 오는데 문제가 발생했습니다.");
		}
		int index = this.excel.getIndex();
		if (index == -1)
		{
			index = 0;
		}
		for (int i = 0, z = transaction.objectSize(); i < z; i++)
		{
			this.dbData.add(transaction.getDataList(i));
		}
	}

	private void readExcel (final String filepath, final int start) throws BiffException, IOException
	{
		final List<String> colType = this.excel.getColType();
		final int maxcols = colType.size();
		try
		{
			final Workbook w = Workbook.getWorkbook(new File(filepath));
			final Sheet sheet = w.getSheet(0);
			int maxrows = 0;
			for (int i = 0; i < 255; i++)
			{
				final int length = sheet.getColumn(i).length;
				if (length == 0)
				{
					continue;
				}
				else if (length > maxrows)
				{
					maxrows = length;
				}
			}
			Constant.getLogger().info("엑셀파일 데이타 갯수(헤더포함) : {}", maxrows);
			final String[][] result = new String[255][(maxrows - start) + 1];
			for (int r = start; r <= maxrows; r++)
			{
				Constant.getLogger().debug("엑셀 데이타 읽기 {}번재 데이타", ((r - start) + 1));
				for (int c = 0; c < maxcols; c++)
				{
					final Cell cell = sheet.getCell(r - 1, c);
					if (cell == null)
					{
						result[c][r - start] = "";
					}
					else
					{
						result[c][r - start] = StringUtil.trim(cell.getContents());
					}
				}
			}
			this.excelParam = Parameter.getInstance();
			for (int c = 0; c < maxcols; c++)
			{
				this.excelParam.put(String.valueOf(c + 1), result[c]);
			}
		}
		catch (final BiffException be)
		{
			Constant.getLogger().error("[{}]에서 엑셀파일을 읽을 때 BiffException 예외가 발생하였습니다.", this.excel.getId());
			throw be;
		}
		catch (final IOException ioe)
		{
			Constant.getLogger().error("[{}]에서 엑셀파일을 읽을 때 IOException 예외가 발생하였습니다.", this.excel.getId());
			throw ioe;
		}
	}

	private void writeDB (final String jdbcID) throws Exception
	{
		this.excelParam.putAll(this.param);
		this.excelParam.printLog();
		final ServiceCaller pf = new ServiceCaller();
		final TransactionValue transaction = pf.executeTransaction(jdbcID, this.excelParam);
		if (transaction.isSuccess())
		{
			for (int i = 0; i < transaction.objectSize(); i++)
			{
				this.applyCount += transaction.getUpdateCount(i);
			}
		}
		else
		{
			throw new Exception("[" + this.excel.getId() + "]에서 데이타베이스를 기록 하는데 문제가 발생했습니다.");
		}
	}

	private void writeExcel (final String filepath) throws FileNotFoundException, IOException, WriteException
	{
		final List<String> colType = this.excel.getColType();
		final FileOutputStream fileOut = new FileOutputStream(filepath);
		try
		{
			final WritableWorkbook wworkbook = Workbook.createWorkbook(new File(filepath));
			int dbIndex = 0;
			for (final DataList dl : this.dbData)
			{
				dbIndex++;
				final WritableSheet wsheet = wworkbook.createSheet("Sheet " + dbIndex, dbIndex - 1);
				wsheet.insertColumn(dl.getFieldCount());
				wsheet.insertRow(dl.size());

				int row = 0;
				while (dl.next())
				{
					for (int k = 0, x = dl.getFieldCount(); k < x; k++)
					{
						if (colType.get(k).equals("string"))
						{
							final Label label = new Label(row, k, dl.getString(k));
							wsheet.addCell(label);
						}
						else if (colType.get(k).equals("int") || colType.get(k).equals("float"))
						{
							final Number number = new Number(row, k, dl.getDouble(k));
							wsheet.addCell(number);
						}
						else if (colType.get(k).equals("date"))
						{
							final DateTime date = new DateTime(row, k, dl.getDate(k));
							wsheet.addCell(date);
						}
						else if (colType.get(k).equals("boolean"))
						{
							final Boolean bool = new Boolean(row, k, dl.getBoolean(k));
							wsheet.addCell(bool);
						}
					}
					row++;
				}
			}
			wworkbook.write();
			wworkbook.close();
		}
		catch (final FileNotFoundException fnfe)
		{
			Constant.getLogger().error("[{}]에서 엑셀파일을 쓸 때 FileNotFoundException 예외가 발생하였습니다.", this.excel.getId());
			throw fnfe;
		}
		catch (final IOException ioe)
		{
			Constant.getLogger().error("[{}]에서 엑셀파일을 쓸 때 IOException 예외가 발생하였습니다.", this.excel.getId());
			throw ioe;
		}
		catch (final WriteException we)
		{
			Constant.getLogger().error("[{}]에서 데이타를 쓸 때 WriteException 예외가 발생하였습니다.", this.excel.getId());
			throw we;
		}
		finally
		{
			fileOut.close();
		}
	}

	private void writeRecordSet ()
	{
		final String[] fields = new String[this.excelParam.size()];

		for (int i = 0, z = this.excelParam.size(); i < z; i++)
		{
			fields[i] = Integer.toString(i + 1);
		}
		this.rsData = new DataList(fields);

		for (int i = 0, z = this.excelParam.getValues("1").length; i < z; i++)
		{
			final Map<String, String> data = new HashMap<String, String>();
			for (int j = 0, y = this.excelParam.size(); j < y; j++)
			{
				data.put(fields[j], this.excelParam.getValues(fields[j])[i]);
			}
			this.rsData.addData(data);
		}
	}
}