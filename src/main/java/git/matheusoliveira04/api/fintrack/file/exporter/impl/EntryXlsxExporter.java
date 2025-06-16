package git.matheusoliveira04.api.fintrack.file.exporter.impl;

import git.matheusoliveira04.api.fintrack.entity.Entry;
import git.matheusoliveira04.api.fintrack.file.exporter.contract.FileExporter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.IntStream;

@Component
public class EntryXlsxExporter implements FileExporter<Entry> {

    private static final String SHEET_NAME = "Entries";
    private static final String[] HEADERS = {"description", "value", "date", "paid", "category_id"};

    @Override
    public Resource exportFile(List<Entry> list) throws Exception {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(SHEET_NAME);

            createHeaderRow(sheet, workbook);
            populateDataRows(sheet, list);
            autoSizeColumn(sheet);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);

            return new ByteArrayResource(outputStream.toByteArray());
        }
    }

    private void createHeaderRow(Sheet sheet, Workbook workbook) {
        Row headerRow = sheet.createRow(0);

        IntStream.range(0, HEADERS.length).forEach(index -> {
            Cell cell = headerRow.createCell(index);
            cell.setCellValue(HEADERS[index]);
            cell.setCellStyle(createHeaderCellStyle(workbook));
        });
    }

    private static void populateDataRows(Sheet sheet, List<Entry> entries) {
        IntStream.range(0, entries.size()).forEach(index -> {
            Row row = sheet.createRow(index + 1);
            row.createCell(0).setCellValue(entries.get(index).getDescription());
            row.createCell(1).setCellValue(entries.get(index).getValue().doubleValue());
            row.createCell(2).setCellValue(entries.get(index).getDate());
            row.createCell(3).setCellValue(entries.get(index).getPaid());
            row.createCell(4).setCellValue(entries.get(index).getCategory().getId().toString());
        });
    }

    private void autoSizeColumn(Sheet sheet) {
        for (int i = 0; i < HEADERS.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private CellStyle createHeaderCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }
}
