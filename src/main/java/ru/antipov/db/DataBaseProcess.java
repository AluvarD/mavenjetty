package ru.antipov.db;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("Bank")
public class DataBaseProcess extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String command = req.getParameter("command");
        String params = req.getParameter("params");
        String result = "";

        boolean error = false;

        try {
            switch (command) {
                case "help":
                    result = "{\"command\":[{\"name\":\"add\",\"params\":\"data\"},{\"name\":\"view\"},{\"name\":\"delete\",\"params\":\"rownum\"}]";
                    break;
                case "add":
                    if (!DataBase.insertData(params)) {
                        result = "\"data inserted\"";
                    } else {
                        error = true;
                    }
                    break;
                case "view":
                    result = "[" + DataBase.selectData() + "]";
                    break;
                case "delete":
                    if (!DataBase.deleteData(Integer.parseInt(params))) {
                        result = "\"data deleted\"";
                    } else {
                        error = true;
                    }
                    break;
                default:
                    error = true;
                    break;
            }
        } catch (Exception ex) {
            error = true;
        }

        if (!error) {
            doSetResult(resp, result);
        } else {
            doSetError(resp);
        }
    }

    protected void doSetResult(HttpServletResponse response, String result) throws UnsupportedEncodingException, IOException {
        String reply = "{\n\t\"error\":0,\n\t\"result\":" + result + "\n}";
        response.getOutputStream().write(reply.getBytes(StandardCharsets.UTF_8));
        response.setContentType("application/json; charset=UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    protected void doSetError(HttpServletResponse response) throws UnsupportedEncodingException, IOException {
        String reply = "{\"error\":1}";
        response.getOutputStream().write(reply.getBytes(StandardCharsets.UTF_8));
        response.setContentType("application/json; charset=UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
