package es.unex.cum.mdai.vrp.utils;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public final class QRCodeGenerator {

	public static void saveQrCode(String texto, String rutaArchivo) throws WriterException, IOException {
		QRCodeWriter escritor = new QRCodeWriter();
		BitMatrix bitMatrix = escritor.encode(texto, BarcodeFormat.QR_CODE, 300, 300);

		Path ruta = FileSystems.getDefault().getPath(rutaArchivo);
		MatrixToImageWriter.writeToPath(bitMatrix, "PNG", ruta);
	}

	public static String generateRouteUrl(String origin, List<String> destinations) throws Exception {
		String baseUrl = "https://www.google.com/maps/dir/?api=1";
		StringBuilder waypoints = new StringBuilder();
		for (String destination : destinations) {
			if (waypoints.length() > 0) {
				waypoints.append("|");
			}
			waypoints.append(URLEncoder.encode(destination, "UTF-8"));
		}
		return String.format("%s&origin=%s&destination=%s&waypoints=%s", baseUrl, origin, origin, waypoints);
	}
}
