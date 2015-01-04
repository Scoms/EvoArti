import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jxl.Cell;
import jxl.CellView;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;



public class ExcelHandler{
	
	public static void Write(WritableSheet sheet, List<Crepe> crepes, int generation) throws BiffException, IOException, RowsExceededException, WriteException
	{
		//Label labelInfo = new Label(0,0, "Les premiers éléments sont les enfants, ensuite viennent les parents");
		Label labelInfo2 = new Label(0,0, "Composition : Oeufs/Lait/farine/sel");
		//sheet.addCell(labelInfo);
		sheet.addCell(labelInfo2);
		
		int rowStart = 2;
		int col = generation * 2;
		
		Label composition = new Label(col,rowStart,"Composition");
		Label eval = new Label(col + 1,rowStart,"Evaluation");

		
		sheet.addCell(composition);
		sheet.addCell(eval);
		
		
		int row = rowStart + 1;
		for (Crepe crepe : crepes) {

			String sComp = String.format("%d / %d / %d / %d", crepe._nbOeufs, crepe._mlLait, crepe._grammeFarine, crepe._pinceSel);
			Label comp = new Label(col,row, sComp);
			jxl.write.Number val = new jxl.write.Number(col + 1,row, Math.round(crepe.eval()));
			
			sheet.addCell(comp);
			sheet.addCell(val);
			
			row++;
		}
		

		CellView cell = sheet.getColumnView(col);
		cell.setAutosize(true);
		sheet.setColumnView(col, cell);
		CellView cell2 = sheet.getColumnView(col + 1);
		cell2.setAutosize(true);
		sheet.setColumnView(col + 1, cell2);
	}
	
	public static void finalise(WritableWorkbook w) throws IOException, WriteException
	{
		
		w.write();
		w.close();
	}

	public static void writeFinal(WritableWorkbook workbookFinal, List<Crepe> crepes) throws RowsExceededException, WriteException {
		
		int col = 0;
		int rowStart = 0;
		Label composition = new Label(col,rowStart,"Index");
		Label eval = new Label(col + 1,rowStart,"Evaluation");

		WritableSheet sheet = workbookFinal.createSheet("First Sheet", 0);
		
		sheet.addCell(composition);
		sheet.addCell(eval);
		
		
		int row = rowStart + 1;
		for (Crepe crepe : crepes) {
			String sComp = String.format("%d / %d / %d / %d", crepe._nbOeufs, crepe._mlLait, crepe._grammeFarine, crepe._pinceSel);
			Label comp = new Label(col,row, sComp);
			jxl.write.Number val = new jxl.write.Number(col + 1,row, Math.round(crepe.eval()));
			
			sheet.addCell(comp);
			sheet.addCell(val);
			
			row++;
		}
		

		CellView cell = sheet.getColumnView(col);
		cell.setAutosize(true);
		sheet.setColumnView(col, cell);
		CellView cell2 = sheet.getColumnView(col + 1);
		cell2.setAutosize(true);
		sheet.setColumnView(col + 1, cell2);

		
		List<Crepe> uniqueCrepes = new ArrayList<Crepe>();
		for (Crepe crepe : crepes) {
			if(!uniqueCrepes.contains(crepe))
			{
				uniqueCrepes.add(crepe);
			}
		}
		
		Label x = new Label(4, rowStart, "Compo");
		Label y = new Label(5, rowStart, "Occurences");
		Label z = new Label(6, rowStart, "Evaluation");

		sheet.addCell(x);
		sheet.addCell(y);
		sheet.addCell(z);
		
		int offset = 0;
		uniqueCrepes.sort(new CrepeComparator());
		for (Crepe crepe : uniqueCrepes) {
			int i = 0;
			
			for (Crepe crepe2 : crepes) {
				if(crepe.equals(crepe2))
					i++;
			}
			String sComp = String.format("%d / %d / %d / %d", crepe._nbOeufs, crepe._mlLait, crepe._grammeFarine, crepe._pinceSel);
			
			Label compo = new Label(4, rowStart + offset + 1, sComp);
			jxl.write.Number num = new jxl.write.Number(5, rowStart + 1 + offset, i);
			jxl.write.Number num2 = new jxl.write.Number(6, rowStart + 1 + offset, Math.round(crepe.eval()));
			sheet.addCell(compo);
			sheet.addCell(num);
			sheet.addCell(num2);
			offset++;
		}
		
		CellView cell3 = sheet.getColumnView(4);
		cell3.setAutosize(true);
		sheet.setColumnView(4, cell3);
		CellView cell4 = sheet.getColumnView(5);
		cell4.setAutosize(true);
		sheet.setColumnView(5, cell4);
	}
	
}