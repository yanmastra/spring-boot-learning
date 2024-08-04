package com.yanmastra.msSecurityBase;

import com.yanmastra.msSecurityBase.utils.ValidationUtils;

public class SimpleTests {
    public static void main(String[] args) {
        String html = "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "    <title>Email</title>" +
                "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1\">" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=Edge\">" +
                "    <style>" +
                "        body {" +
                "            margin: 0;" +
                "        }" +
                "" +
                "        body," +
                "        p," +
                "        div {" +
                "            background-color: white;" +
                "            text-align: center;" +
                "            font-family: Arial, Helvetica, sans-serif;" +
                "            font-size: 14px;" +
                "            letter-spacing: 0.2px;" +
                "            line-height: 18pt;" +
                "        }" +
                "" +
                "        .code {" +
                "            font-family: 'Courier New', Courier, monospace;" +
                "            font-weight: 600;" +
                "            font-size: 14pt;" +
                "        }" +
                "" +
                "        .page {" +
                "            max-width: 512px;" +
                "            text-align: center;" +
                "        }" +
                "" +
                "        .page-header-space {" +
                "            height: 220px;" +
                "" +
                "        }" +
                "" +
                "        .page-footer-space {" +
                "            height: 90px;" +
                "" +
                "        }" +
                "" +
                "        .page-header {" +
                "            line-height: 0;" +
                "            position: fixed;" +
                "            top: 0mm;" +
                "            width: 100%;" +
                "            background: #b4b4b4;" +
                "            padding-top: 40px;" +
                "            padding-bottom: 40px;" +
                "            height: 104px;" +
                "            text-align: center;" +
                "" +
                "        }" +
                "" +
                "        .page-footer {" +
                "            position: fixed;" +
                "            max-width: 512px;" +
                "            text-align: center;" +
                "            font-size: 10pt;" +
                "            padding-top: 20px;" +
                "            padding-bottom: 20px;" +
                "" +
                "        }" +
                "" +
                "        .page {" +
                "            page-break-after: auto;" +
                "            padding: 0 16px;" +
                "            margin-top: 40px;" +
                "" +
                "        }" +
                "" +
                "        .page p," +
                "        .page li {" +
                "            font-size: 10pt;" +
                "            text-align: left;" +
                "" +
                "        }" +
                "" +
                "        table {" +
                "            border-collapse: collapse;" +
                "        }" +
                "" +
                "        .content-table," +
                "        .content-table tr," +
                "        .content-table td {" +
                "            border: 1px solid #000;" +
                "" +
                "        }" +
                "" +
                "        .content-table {" +
                "            width: 100%;" +
                "            table-layout: fixed;" +
                "" +
                "        }" +
                "" +
                "        .content-table td {" +
                "            text-align: left;" +
                "            padding: 8px;" +
                "            font-size: 10pt;" +
                "" +
                "        }" +
                "" +
                "        .pg-break {" +
                "            page-break-before: always;" +
                "" +
                "        }" +
                "" +
                "        .checkboxtext {" +
                "            word-wrap: break-word;" +
                "" +
                "        }" +
                "" +
                "        input[type=checkbox] {" +
                "            -ms-transform: scale(2);" +
                "            -moz-transform: scale(2);" +
                "            -webkit-transform: scale(2);" +
                "            -o-transform: scale(2);" +
                "            transform: scale(2);" +
                "            padding: 10px;" +
                "            margin: 20px 20px 20px 10px" +
                "        }" +
                "" +
                "        .signature-component {" +
                "            height: 300px;" +
                "" +
                "        }" +
                "" +
                "        .signature {" +
                "            width: 90%;" +
                "            height: auto;" +
                "" +
                "        }" +
                "" +
                "        @page {" +
                "            size: A4 portrait;" +
                "            position: relative;" +
                "            margin: 48mm 0 25mm 0;" +
                "" +
                "        }" +
                "" +
                "        .bold {" +
                "            font-weight: bold;" +
                "" +
                "        }" +
                "" +
                "        @media screen {" +
                "" +
                "            .page-header," +
                "            .page-footer {" +
                "                position: static" +
                "            }" +
                "" +
                "            .page-header-space," +
                "            .page-footer-space {" +
                "                height: 0px;" +
                "" +
                "            }" +
                "" +
                "" +
                "        }" +
                "" +
                "        @media print {" +
                "            thead {" +
                "                display: table-header-group;" +
                "" +
                "            }" +
                "" +
                "            tfoot {" +
                "                display: table-footer-group;" +
                "" +
                "            }" +
                "" +
                "            body {" +
                "                margin: 0;" +
                "" +
                "            }" +
                "" +
                "" +
                "        }" +
                "" +
                "        table.wrapper {" +
                "            width: 100% !important;" +
                "            table-layout: fixed;" +
                "            -webkit-font-smoothing: antialiased;" +
                "            -webkit-text-size-adjust: 100%;" +
                "            -moz-text-size-adjust: 100%;" +
                "            -ms-text-size-adjust: 100%;" +
                "        }" +
                "" +
                "        table td {" +
                "            text-align: left;" +
                "            padding-right: 4px;" +
                "        }" +
                "" +
                "        .button {" +
                "            font-family: Arial, Helvetica, sans-serif;" +
                "            font-weight: 600;" +
                "            text-decoration: none;" +
                "            color: #fff;" +
                "            padding: 12px 24px;" +
                "            margin: 4px 16px;" +
                "            background-color: #019310;" +
                "            border-radius: 4px;" +
                "        }" +
                "    </style>" +
                "</head>" +
                "" +
                "<body>" +
                "    <center>" +
                "        <div class=\"page-header\">" +
                "            <img src=\"https://avatars.githubusercontent.com/u/22540516?v=4&size=104\" height=\"104px\"" +
                "                alt=\"Mahotama Logo\" />" +
                "        </div>" +
                "   </center>" +
                "</body>" +
                "</html>";
        boolean result = ValidationUtils.isHtml(html);
        System.out.println("result: " + result);

        String text = "text biasa laaah, hahaha";
        result = ValidationUtils.isHtml(text);
        System.out.println("result: " + result);
    }
}
