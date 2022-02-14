package com.farmaciasperuanas.pmmli.monitor.service;

import com.farmaciasperuanas.pmmli.monitor.dto.ResponseApi;
import com.farmaciasperuanas.pmmli.monitor.dto.ResponseApiErrorItem;
import com.farmaciasperuanas.pmmli.monitor.dto.ResponseDto;
import com.farmaciasperuanas.pmmli.monitor.dto.ResponseMasterTransactionDto;
import com.farmaciasperuanas.pmmli.monitor.repository.ConfigMonitorRepository;
import com.farmaciasperuanas.pmmli.monitor.util.Constants;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class EndpointServiceImpl implements EndpointService {

    private static final Gson GSON = new Gson();

    @Autowired
    MailService mailService;
    @Autowired
    ConfigMonitorRepository configMonitorRepository;

    @Override
    public ResponseMasterTransactionDto ejecutarProceso(String typeOp) {

        ResponseMasterTransactionDto responseDto = new ResponseMasterTransactionDto();
        String urlString = "";
        String master = "";
        try {
            if (typeOp.equalsIgnoreCase(Constants.TYPE_PROVEEDORES)) {
                urlString = Constants.URL_PROVEEDORES;
                master = "PROVEEDORES";
            } else if (typeOp.equalsIgnoreCase(Constants.TYPE_BARCODE)) {
                urlString = Constants.URL_BARCODE;
                master = "CÓDIGOS DE BARRA";
            } else if (typeOp.equalsIgnoreCase(Constants.TYPE_MATERIAL)) {
                urlString = Constants.URL_MATERIAL;
                master = "MATERIALES";
            } else if (typeOp.equalsIgnoreCase(Constants.TYPE_TIENDAS)) {
                urlString = Constants.URL_TIENDAS;
                master = "TIENDAS";
            } else if (typeOp.equalsIgnoreCase(Constants.TYPE_DATOS_VOLUMETRICOS)) {
                urlString = Constants.URL_DATOS_VOLUMETRICOS;
                master = "DATOS VOLUMÉTRICOS";
            } else if (typeOp.equalsIgnoreCase(Constants.TYPE_MATERIAL_LOT)) {
                urlString = Constants.URL_MATERIAL_LOT;
                master = "MATERIALES LOTE";
            } else if (typeOp.equalsIgnoreCase(Constants.TYPE_MATERIAL_PROVEEDOR)) {
                urlString = Constants.URL_MATERIAL_PROVEEDOR;
                master = "MATERIALES PROVEEDOR";
            }

            URL url = new URL(urlString);
            URLConnection urlConnection = url.openConnection();

            HttpURLConnection httpUrlConnection = (HttpURLConnection) urlConnection;
            httpUrlConnection.setConnectTimeout(30_000);
            httpUrlConnection.setReadTimeout(30_000);
            httpUrlConnection.setDoInput(true);
            httpUrlConnection.setDoOutput(true);
            httpUrlConnection.setRequestProperty("Authorization","Basic d2Vic2VydmljZTpTV0xJMjAyMg==");
            httpUrlConnection.setRequestProperty("Content-Type", "application/json");
            httpUrlConnection.setRequestProperty("Accept", "application/json");
            httpUrlConnection.setRequestMethod("POST");
            OutputStream os = httpUrlConnection.getOutputStream();
            os.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(httpUrlConnection.getInputStream(), StandardCharsets.UTF_8));
            String outPut;
            StringBuilder sb = new StringBuilder();

            while ((outPut = br.readLine()) != null) {
                sb.append(outPut);
            }
            responseDto = GSON.fromJson(sb.toString(), ResponseMasterTransactionDto.class);

            httpUrlConnection.disconnect();
            if (!responseDto.isStatus()) {
                String subject = "[PMM-LI] ERROR EN EL ENVIO DE MAESTRO " + master + " - "+ StringUtils.leftPad("" + responseDto.getBody().getId(), 6, "0");
                String newBody = "<html><head>" +
                        "<style>table { border-collapse: collapse;} table, td, th { border: 1px solid black; padding: 5px;}</style>"
                        + "</head><body><p>";
                Integer errors = 0;
                if (responseDto.getBody().getErrors() != null) {
                    errors = responseDto.getBody().getErrors().size();
                }
                newBody += "Cantidad de errores encontrados: " + errors;
                newBody += "<br/>";
                if (responseDto.getBody().getErrors() != null) {
                    newBody += getDetailInfo(responseDto.getBody().getErrors());
                }
                newBody += "</p>" +
                        " <p> Saludos, el equipo de PMM-LI </p>" +
                        "</body></html>";
                mailService.sendEmail(subject, "edu.david2706@gmail.com", newBody);
            }

        } catch (Exception e) {
            responseDto.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatus(false);
            responseDto.setMessage(e.getMessage());
            e.printStackTrace();
        }
        return responseDto;
    }

    private String getDetailInfo(List<ResponseApiErrorItem> observations) {
        String table = "";
        table += "</br></br><table>";
        table += "<thead>";
        table += "<tr>";
        table += "<td>Identificador</td>";
        table += "<td>Mensaje</td>";
        table += "</tr>";
        table += "</thead>";
        table += "<tbody>";
        for (ResponseApiErrorItem dto : observations) {
            table += "<tr>";
            table += "<td>" + dto.getPk() + "</td>";
            table += "<td>" + dto.getMessage() + "</td>";
            table += "</tr>";
        }
        table += "</tbody>";
        table += "</table>";

        return table;
    }
}
