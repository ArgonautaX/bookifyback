package com.integrador.bookifyback.controller;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.integrador.bookifyback.domain.reporte.ReporteVentasResponse;
import com.integrador.bookifyback.domain.reporte.ReporteService;

@RestController
@RequestMapping("/api/reportes")
@PreAuthorize("hasRole('ADMIN')")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping("/ventas")
    public ResponseEntity<?> reporteVentas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin,
            @RequestParam(required = false, defaultValue = "json") String formato) {

        switch (formato.toLowerCase()) {
            case "pdf":
                byte[] pdf = reporteService.exportarPdf(fechaInicio, fechaFin);
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION,
                                "attachment; filename=reporte-ventas.pdf")
                        .contentType(MediaType.APPLICATION_PDF)
                        .body(pdf);

            case "xlsx":
                byte[] xlsx = reporteService.exportarExcel(fechaInicio, fechaFin);
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION,
                                "attachment; filename=reporte-ventas.xlsx")
                        .contentType(MediaType.parseMediaType(
                                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                        .body(xlsx);

            case "csv":
                byte[] csv = reporteService.exportarCsv(fechaInicio, fechaFin);
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION,
                                "attachment; filename=reporte-ventas.csv")
                        .contentType(MediaType.parseMediaType("text/csv"))
                        .body(csv);

            default:
                ReporteVentasResponse response = reporteService.generarReporteVentas(fechaInicio, fechaFin);
                return ResponseEntity.ok(response);
        }
    }
}
