package ru.antipov.calculator;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("Calculator")
public class Calculator extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String param_a = req.getParameter("a");
        String param_b = req.getParameter("b");
        String param_operation = req.getParameter("operation");

        double value_a = 0;
        double value_b = 0;

        boolean error = false;

        try {
            value_a = Double.parseDouble(param_a);
            value_b = Double.parseDouble(param_b);
        } catch (Exception ex) {
            error = true;
        }

        if (!error) {
            double result = 0;

            try {
                switch (param_operation) {
                    case "+":
                        result = getSum(value_a, value_b);
                        break;
                    case "-":
                        result = getDif(value_a, value_b);
                        break;
                    case "*":
                        result = getMultiply(value_a, value_b);
                        break;
                    case "/":
                        result = getDiv(value_a, value_b);
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
                return;
            }
        }

        doSetError(resp);
    }

    protected void doSetResult(HttpServletResponse response, double result) throws UnsupportedEncodingException, IOException {
        String reply = "{\n    \"error\":0,\n    \"result\":" + Double.toString(result) + "\n}";
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

    protected double getSum(double a, double b) {
        return a + b;
    }

    protected double getDif(double a, double b) {
        return a - b;
    }

    protected double getMultiply(double a, double b) {
        return a * b;
    }

    protected double getDiv(double a, double b) {
        return a / b;
    }
}
