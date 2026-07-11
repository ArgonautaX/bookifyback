package com.integrador.bookifyback.domain.reporte;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.integrador.bookifyback.domain.compra.CompraRepository;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;

@Service
public class ReporteService {

    private final CompraRepository compraRepository;

    public ReporteService(CompraRepository compraRepository) {
        this.compraRepository = compraRepository;
    }

    public ReporteVentasResponse generarReporteVentas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        ReporteVentasResumenDto resumen = obtenerResumen(fechaInicio, fechaFin);
        List<ReporteTopLibroDto> topLibros = obtenerTopLibros(fechaInicio, fechaFin);
        List<ReporteVentasCategoriaDto> ventasPorCategoria = obtenerVentasPorCategoria(fechaInicio, fechaFin);
        List<ReporteVentasAutorDto> ventasPorAutor = obtenerVentasPorAutor(fechaInicio, fechaFin);

        return new ReporteVentasResponse(resumen, topLibros, ventasPorCategoria, ventasPorAutor);
    }

    public ReporteVentasResumenDto obtenerResumen(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        Long cantidad = compraRepository.countCompletadasByFechaBetween(fechaInicio, fechaFin);
        BigDecimal ingresos = compraRepository.sumIngresosByFechaBetween(fechaInicio, fechaFin);
        return new ReporteVentasResumenDto(cantidad, ingresos, cantidad, fechaInicio, fechaFin);
    }

    public List<ReporteTopLibroDto> obtenerTopLibros(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        List<Object[]> resultados = compraRepository.findTopLibrosVendidos(fechaInicio, fechaFin, PageRequest.of(0, 10));
        return resultados.stream().map(r -> new ReporteTopLibroDto(
                (String) r[0],
                (String) r[1],
                (String) r[2],
                (Long) r[3],
                (BigDecimal) r[4]
        )).toList();
    }

    public List<ReporteVentasCategoriaDto> obtenerVentasPorCategoria(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        List<Object[]> resultados = compraRepository.findVentasByCategoria(fechaInicio, fechaFin);
        return resultados.stream().map(r -> new ReporteVentasCategoriaDto(
                (String) r[0],
                (Long) r[1],
                (BigDecimal) r[2]
        )).toList();
    }

    public List<ReporteVentasAutorDto> obtenerVentasPorAutor(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        List<Object[]> resultados = compraRepository.findVentasByAutor(fechaInicio, fechaFin);
        return resultados.stream().map(r -> new ReporteVentasAutorDto(
                (String) r[0],
                (Long) r[1],
                (BigDecimal) r[2]
        )).toList();
    }

    public byte[] exportarPdf(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        try {
            ReporteVentasResponse reporte = generarReporteVentas(fechaInicio, fechaFin);

            JasperReport jasperReport = JasperCompileManager.compileReport(
                getClass().getResourceAsStream("/reports/ReporteVentas.jrxml")
            );

            Map<String, Object> params = new HashMap<>();
            params.put("fechaInicio", fechaInicio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            params.put("fechaFin", fechaFin.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            params.put("totalVentas", reporte.resumen().totalVentas());
            params.put("ingresosTotales", reporte.resumen().ingresosTotales());
            params.put("cantidadTransacciones", reporte.resumen().cantidadTransacciones());

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(reporte.topLibros());
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);

            return JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (JRException e) {
            throw new RuntimeException("Error al generar el reporte PDF", e);
        }
    }

    public byte[] exportarExcel(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        try {
            ReporteVentasResponse reporte = generarReporteVentas(fechaInicio, fechaFin);

            JasperReport jasperReport = JasperCompileManager.compileReport(
                getClass().getResourceAsStream("/reports/ReporteVentas.jrxml")
            );

            Map<String, Object> params = new HashMap<>();
            params.put("fechaInicio", fechaInicio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            params.put("fechaFin", fechaFin.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            params.put("totalVentas", reporte.resumen().totalVentas());
            params.put("ingresosTotales", reporte.resumen().ingresosTotales());
            params.put("cantidadTransacciones", reporte.resumen().cantidadTransacciones());

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(reporte.topLibros());
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            JRXlsxExporter exporter = new JRXlsxExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
            exporter.exportReport();

            return out.toByteArray();
        } catch (JRException e) {
            throw new RuntimeException("Error al generar el reporte Excel", e);
        }
    }

    public byte[] exportarCsv(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        try {
            ReporteVentasResponse reporte = generarReporteVentas(fechaInicio, fechaFin);

            JasperReport jasperReport = JasperCompileManager.compileReport(
                getClass().getResourceAsStream("/reports/ReporteVentas.jrxml")
            );

            Map<String, Object> params = new HashMap<>();
            params.put("fechaInicio", fechaInicio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            params.put("fechaFin", fechaFin.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            params.put("totalVentas", reporte.resumen().totalVentas());
            params.put("ingresosTotales", reporte.resumen().ingresosTotales());
            params.put("cantidadTransacciones", reporte.resumen().cantidadTransacciones());

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(reporte.topLibros());
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            JRCsvExporter exporter = new JRCsvExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleWriterExporterOutput(out));
            exporter.exportReport();

            return out.toByteArray();
        } catch (JRException e) {
            throw new RuntimeException("Error al generar el reporte CSV", e);
        }
    }
}
