package com.ecommerce.controller;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ecommerce.entity.OrderItem;
import com.ecommerce.entity.Orders;
import com.ecommerce.repository.OrderItemRepository;
import com.ecommerce.repository.OrdersRepository;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class InvoiceController {

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

private byte[] cachedLogoBytes;
    // =========================================================
    // VIEW INVOICE
    // =========================================================

    @GetMapping("/invoice/{id}")
    public String invoice(
            @PathVariable Long id,
            Model model) {

        Orders order =
                ordersRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Order Not Found"));

        List<OrderItem> items =
                orderItemRepository.findByOrder(order);

        model.addAttribute(
                "order",
                order);

        model.addAttribute(
                "items",
                items);

        return "invoice";
    }


    // =========================================================
    // DOWNLOAD PDF
    // =========================================================

    @GetMapping("/invoice/pdf/{id}")
    public void downloadInvoice(
            @PathVariable Long id,
            HttpServletResponse response)
            throws Exception {

        Orders order =
                ordersRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Order Not Found"));

        byte[] pdf =
                generateInvoicePdf(order);

        response.setContentType(
                "application/pdf");

        response.setHeader(
                "Content-Disposition",
                "attachment; filename=SRIG_Invoice_"
                        + order.getId()
                        + ".pdf");

        response.getOutputStream()
                .write(pdf);

        response.getOutputStream()
                .flush();
    }


    // =========================================================
    // GENERATE PDF
    //
    // THIS METHOD IS ALSO USED BY EMAIL
    // =========================================================

    public byte[] generateInvoicePdf(
            Orders order)
            throws Exception {

        List<OrderItem> items =
                orderItemRepository.findByOrder(order);


        ByteArrayOutputStream output =
                new ByteArrayOutputStream();


        // =====================================================
        // A4 DOCUMENT
        // =====================================================

        Document document =
                new Document(
                        PageSize.A4,
                        30,
                        30,
                        25,
                        25);


        PdfWriter writer =
                PdfWriter.getInstance(
                        document,
                        output);


        document.open();


        // =====================================================
        // COLORS
        // =====================================================

        Color cream =
                new Color(
                        249,
                        247,
                        241);

        Color gold =
                new Color(
                        157,
                        119,
                        51);

        Color dark =
                new Color(
                        28,
                        28,
                        28);

        Color gray =
                new Color(
                        95,
                        95,
                        95);

        Color lightGold =
                new Color(
                        235,
                        226,
                        207);


        // =====================================================
        // BACKGROUND
        // =====================================================

        PdfContentByte background =
                writer.getDirectContentUnder();

        background.setColorFill(
                cream);

        background.rectangle(
                0,
                0,
                PageSize.A4.getWidth(),
                PageSize.A4.getHeight());

        background.fill();


        // =====================================================
        // REPEATED SRIG WATERMARK
        // =====================================================

        
                // LOWER MIDDLE
         
        // =====================================================
        // FONTS
        // =====================================================

        Font brandFont =
                new Font(
                        Font.HELVETICA,
                        20,
                        Font.BOLD,
                        dark);

        Font taglineFont =
                new Font(
                        Font.HELVETICA,
                        7,
                        Font.NORMAL,
                        gold);

        Font invoiceFont =
                new Font(
                        Font.HELVETICA,
                        24,
                        Font.BOLD,
                        gold);

        Font sectionFont =
                new Font(
                        Font.HELVETICA,
                        8,
                        Font.BOLD,
                        gold);

        Font headingFont =
                new Font(
                        Font.HELVETICA,
                        8,
                        Font.BOLD,
                        dark);

        Font normalFont =
                new Font(
                        Font.HELVETICA,
                        7.5f,
                        Font.NORMAL,
                        dark);

        Font smallFont =
                new Font(
                        Font.HELVETICA,
                        6.8f,
                        Font.NORMAL,
                        gray);

        Font tableHeaderFont =
                new Font(
                        Font.HELVETICA,
                        7,
                        Font.BOLD,
                        Color.WHITE);

        Font totalFont =
                new Font(
                        Font.HELVETICA,
                        10,
                        Font.BOLD,
                        dark);


        // =====================================================
        // HEADER
        // =====================================================

        PdfPTable header =
                new PdfPTable(2);

        header.setWidthPercentage(100);

        header.setWidths(
                new float[]{
                        58,
                        42
                });


        // =====================================================
        // BRAND
        // =====================================================

        PdfPCell brandCell =
                new PdfPCell();

        brandCell.setBorder(
                Rectangle.NO_BORDER);

        brandCell.setPadding(0);


       Image logo;

if (cachedLogoBytes == null) {

    URL logoUrl =
            getClass()
                    .getResource(
                            "/static/images/srig-logo.png");

    if (logoUrl != null) {

        try (InputStream input =
                     logoUrl.openStream()) {

            cachedLogoBytes =
                    input.readAllBytes();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}

logo =
        cachedLogoBytes != null
                ? Image.getInstance(
                        cachedLogoBytes)
                : null;

        if (logo != null) {

            logo.scaleToFit(
                    95,
                    75);

            brandCell.addElement(
                    logo);
        }


        brandCell.addElement(
                new Paragraph(
                        "SRIG ESSENSE",
                        brandFont));


        brandCell.addElement(
                new Paragraph(
                        "OWN YOUR STYLE",
                        taglineFont));


        Paragraph company =
                new Paragraph();

        company.setSpacingBefore(5);

        company.add(
                new Phrase(
                        "Kalyandurg, Ananthapur\n",
                        smallFont));

        company.add(
                new Phrase(
                        "Andhra Pradesh - 515761\n",
                        smallFont));

        company.add(
                new Phrase(
                        "+91 9392694740\n",
                        smallFont));

        company.add(
                new Phrase(
                        "support@srigessense.com\n",
                        smallFont));

        company.add(
                new Phrase(
                        "www.srig.com",
                        smallFont));

        brandCell.addElement(company);

        header.addCell(brandCell);


        // =====================================================
        // INVOICE DETAILS
        // =====================================================

        PdfPCell invoiceCell =
                new PdfPCell();

        invoiceCell.setBorder(
                Rectangle.NO_BORDER);

        invoiceCell.setPadding(0);


        Paragraph invoiceTitle =
                new Paragraph(
                        "INVOICE",
                        invoiceFont);

        invoiceTitle.setAlignment(
                Element.ALIGN_RIGHT);

        invoiceCell.addElement(
                invoiceTitle);


        invoiceCell.addElement(
                rightInfo(
                        "Invoice No : ",
                        "INV-" + order.getId(),
                        headingFont,
                        normalFont));


        invoiceCell.addElement(
                rightInfo(
                        "Order ID : ",
                        String.valueOf(
                                order.getId()),
                        headingFont,
                        normalFont));


        invoiceCell.addElement(
                rightInfo(
                        "Date : ",
                        safe(
                                order.getPlacedAt()),
                        headingFont,
                        normalFont));


        header.addCell(
                invoiceCell);

        document.add(header);


        document.add(
                divider(gold));


        // =====================================================
        // BILLING / SHIPPING
        // =====================================================

        PdfPTable address =
                new PdfPTable(2);

        address.setWidthPercentage(100);

        address.setWidths(
                new float[]{
                        50,
                        50
                });

        address.setSpacingBefore(5);


        // BILLING

        PdfPCell billing =
                informationCell();

        billing.addElement(
                new Paragraph(
                        "BILLING ADDRESS",
                        sectionFont));

        billing.addElement(
                new Paragraph(
                        safe(
                                order.getCustomerName()),
                        headingFont));

        billing.addElement(
                new Paragraph(
                        "Email : "
                                + safe(
                                order.getEmail()),
                        normalFont));

        billing.addElement(
                new Paragraph(
                        "Mobile : "
                                + safe(
                                order.getMobile()),
                        normalFont));

        billing.addElement(
                new Paragraph(
                        "Address : "
                                + safe(
                                order.getAddress()),
                        normalFont));

        billing.addElement(
                new Paragraph(
                        safe(
                                order.getCity())
                                + " - "
                                + safe(
                                order.getPincode()),
                        normalFont));

        address.addCell(billing);


        // SHIPPING

        PdfPCell shipping =
                informationCell();

        shipping.addElement(
                new Paragraph(
                        "SHIPPING ADDRESS",
                        sectionFont));

        shipping.addElement(
                new Paragraph(
                        safe(
                                order.getCustomerName()),
                        headingFont));

        shipping.addElement(
                new Paragraph(
                        "Mobile : "
                                + safe(
                                order.getMobile()),
                        normalFont));

        shipping.addElement(
                new Paragraph(
                        "Address : "
                                + safe(
                                order.getAddress()),
                        normalFont));

        shipping.addElement(
                new Paragraph(
                        safe(
                                order.getCity())
                                + " - "
                                + safe(
                                order.getPincode()),
                        normalFont));

        address.addCell(shipping);


        document.add(address);


        // =====================================================
        // ORDERED PRODUCTS
        // =====================================================

        Paragraph productTitle =
                new Paragraph(
                        "ORDERED PRODUCTS",
                        sectionFont);

        productTitle.setSpacingBefore(8);

        productTitle.setSpacingAfter(4);

        document.add(productTitle);


        PdfPTable productTable =
                new PdfPTable(4);

        productTable.setWidthPercentage(100);

        productTable.setWidths(
                new float[]{
                        47,
                        18,
                        12,
                        23
                });


        headerCell(
                productTable,
                "PRODUCT",
                tableHeaderFont,
                dark);

        headerCell(
                productTable,
                "PRICE",
                tableHeaderFont,
                dark);

        headerCell(
                productTable,
                "QTY",
                tableHeaderFont,
                dark);

        headerCell(
                productTable,
                "TOTAL",
                tableHeaderFont,
                dark);


        for (OrderItem item : items) {

            String productName =
                    item.getProduct() != null
                            ? safe(
                            item.getProduct()
                                    .getName())
                            : "Product";

            double price =
                    item.getPrice();

            int quantity =
                    item.getQuantity();

            double total =
                    price * quantity;


            productCell(
                    productTable,
                    productName,
                    normalFont,
                    Element.ALIGN_LEFT);

            productCell(
                    productTable,
                    "₹ "
                            + String.format(
                            "%.2f",
                            price),
                    normalFont,
                    Element.ALIGN_RIGHT);

            productCell(
                    productTable,
                    String.valueOf(
                            quantity),
                    normalFont,
                    Element.ALIGN_CENTER);

            productCell(
                    productTable,
                    "₹ "
                            + String.format(
                            "%.2f",
                            total),
                    normalFont,
                    Element.ALIGN_RIGHT);
        }


        document.add(productTable);


        // =====================================================
        // ORDER SUMMARY
        // =====================================================

        PdfPTable summary =
                new PdfPTable(2);

        summary.setWidthPercentage(43);

        summary.setWidths(
                new float[]{
                        58,
                        42
                });

        summary.setHorizontalAlignment(
                Element.ALIGN_RIGHT);

        summary.setSpacingBefore(8);


        summaryRow(
                summary,
                "Subtotal",
                "₹ "
                        + String.format(
                        "%.2f",
                        order.getTotalAmount()),
                normalFont);


        summaryRow(
                summary,
                "Delivery Charges",
                "FREE",
                normalFont);


        summaryRow(
                summary,
                "GST",
                "Included",
                normalFont);


        PdfPCell grandLabel =
                new PdfPCell(
                        new Phrase(
                                "GRAND TOTAL",
                                totalFont));

        grandLabel.setBackgroundColor(
                lightGold);

        grandLabel.setBorder(
                Rectangle.NO_BORDER);

        grandLabel.setPadding(7);


        PdfPCell grandValue =
                new PdfPCell(
                        new Phrase(
                                "₹ "
                                        + String.format(
                                        "%.2f",
                                        order.getTotalAmount()),
                                totalFont));

        grandValue.setBackgroundColor(
                lightGold);

        grandValue.setBorder(
                Rectangle.NO_BORDER);

        grandValue.setPadding(7);

        grandValue.setHorizontalAlignment(
                Element.ALIGN_RIGHT);


        summary.addCell(grandLabel);

        summary.addCell(grandValue);

        document.add(summary);


        // =====================================================
        // PAYMENT / DELIVERY
        // =====================================================

        PdfPTable details =
                new PdfPTable(2);

        details.setWidthPercentage(100);

        details.setWidths(
                new float[]{
                        50,
                        50
                });

        details.setSpacingBefore(8);


        // PAYMENT

        PdfPCell payment =
                informationCell();

        payment.addElement(
                new Paragraph(
                        "PAYMENT DETAILS",
                        sectionFont));

        payment.addElement(
                new Paragraph(
                        "Payment Method : "
                                + safe(
                                order.getPaymentMethod()),
                        normalFont));

        payment.addElement(
                new Paragraph(
                        "Payment Status : "
                                + safe(
                                order.getPaymentStatus()),
                        normalFont));


        if (order.getTransactionId()
                != null) {

            payment.addElement(
                    new Paragraph(
                            "Transaction ID : "
                                    + safe(
                                    order.getTransactionId()),
                            normalFont));
        }


        details.addCell(payment);


        // DELIVERY

        PdfPCell delivery =
                informationCell();

        delivery.addElement(
                new Paragraph(
                        "DELIVERY DETAILS",
                        sectionFont));


        if (order.getCourierName()
                != null) {

            delivery.addElement(
                    new Paragraph(
                            "Courier : "
                                    + safe(
                                    order.getCourierName()),
                            normalFont));
        }


        if (order.getTrackingNumber()
                != null) {

            delivery.addElement(
                    new Paragraph(
                            "Tracking Number : "
                                    + safe(
                                    order.getTrackingNumber()),
                            normalFont));
        }


        delivery.addElement(
                new Paragraph(
                        "Order Status : "
                                + safe(
                                order.getStatus()),
                        normalFont));


        if (order.getExpectedDeliveryDate()
                != null) {

            delivery.addElement(
                    new Paragraph(
                            "Expected Delivery : "
                                    + safe(
                                    order.getExpectedDeliveryDate()),
                            normalFont));
        }


        details.addCell(delivery);

        document.add(details);


        // =====================================================
        // THANK YOU + SIGNATURE
        // =====================================================

        PdfPTable footer =
                new PdfPTable(2);

        footer.setWidthPercentage(100);

        footer.setWidths(
                new float[]{
                        58,
                        42
                });

        footer.setSpacingBefore(12);


        // THANK YOU

        PdfPCell thankCell =
                new PdfPCell();

        thankCell.setBorder(
                Rectangle.NO_BORDER);


        thankCell.addElement(
                new Paragraph(
                        "THANK YOU FOR CHOOSING",
                        headingFont));


        thankCell.addElement(
                new Paragraph(
                        "SRIG ESSENSE",
                        new Font(
                                Font.HELVETICA,
                                14,
                                Font.BOLD,
                                gold)));


        thankCell.addElement(
                new Paragraph(
                        "OWN YOUR STYLE",
                        taglineFont));


        thankCell.addElement(
                new Paragraph(
                        "For any queries:\n"
                                + "support@srigessense.com\n"
                                + "+91 9392694740",
                        smallFont));


        footer.addCell(thankCell);


        // SIGNATURE

        PdfPCell signatureCell =
                new PdfPCell();

        signatureCell.setBorder(
                Rectangle.NO_BORDER);

        signatureCell.setHorizontalAlignment(
                Element.ALIGN_CENTER);


        Paragraph signatureTitle =
                new Paragraph(
                        "AUTHORIZED SIGNATURE",
                        sectionFont);

        signatureTitle.setAlignment(
                Element.ALIGN_CENTER);

        signatureCell.addElement(
                signatureTitle);


        Image signature =
        loadSignatureImage(
                "/static/images/signature.jpeg");

        if (signature != null) {

            signature.scaleToFit(
                    125,
                    60);

            // YOUR SIGNATURE ROTATION


            signature.setAlignment(
                    Element.ALIGN_CENTER);

            signatureCell.addElement(
                    signature);
        }


        Paragraph signatureName =
                new Paragraph(
                        "SRIG ESSENSE",
                        smallFont);

        signatureName.setAlignment(
                Element.ALIGN_CENTER);

        signatureCell.addElement(
                signatureName);


        footer.addCell(
                signatureCell);

        document.add(footer);


        // =====================================================
        // FOOTER
        // =====================================================

        document.add(
                divider(gold));


        Paragraph finalText =
                new Paragraph(
                        "This is a computer-generated invoice "
                                + "and does not require a signature.\n"
                                + "support@srigessense.com  |  "
                                + "+91 9392694740",
                        smallFont);

        finalText.setAlignment(
                Element.ALIGN_CENTER);

        document.add(finalText);


        Paragraph website =
                new Paragraph(
                        "www.srig.com",
                        taglineFont);

        website.setAlignment(
                Element.ALIGN_CENTER);

        document.add(website);


        // =====================================================
        // CLOSE
        // =====================================================

        document.close();


        return output.toByteArray();
    }


  // =========================================================
// LOAD IMAGE
// =========================================================

private Image loadImage(
        String path) {

    try {

        URL url =
                getClass()
                        .getResource(path);

        if (url == null) {

            System.out.println(
                    "IMAGE NOT FOUND : "
                            + path);

            return null;
        }

        return Image.getInstance(url);

    } catch (Exception e) {

        e.printStackTrace();

        return null;
    }
}


// =========================================================
// LOAD SIGNATURE IMAGE
// =========================================================
// =============
// =========================================================
// LOAD SIGNATURE IMAGE
// =========================================================

private Image loadSignatureImage(
        String path) {

    try {

        URL url =
                getClass()
                        .getResource(path);

        if (url == null) {

            System.out.println(
                    "SIGNATURE NOT FOUND : "
                            + path);

            return null;
        }

        BufferedImage original =
                ImageIO.read(url);

        if (original == null) {

            return null;
        }

        int width =
                original.getWidth();

        int height =
                original.getHeight();

        BufferedImage rotated =
                new BufferedImage(
                        height,
                        width,
                        BufferedImage.TYPE_INT_RGB);

        Graphics2D graphics =
                rotated.createGraphics();

        graphics.translate(
                0,
                width);

        graphics.rotate(
                Math.toRadians(-90));

        graphics.drawImage(
                original,
                0,
                0,
                null);

        graphics.dispose();

        ByteArrayOutputStream output =
                new ByteArrayOutputStream();

        ImageIO.write(
                rotated,
                "jpg",
                output);

        return Image.getInstance(
                output.toByteArray());

    } catch (Exception e) {

        e.printStackTrace();

        return null;
    }
}
// TRANSPARENT WATERMARK
// =========================================================

private Image loadTransparentLogo(
        String path) {

    try {

        InputStream input =
                getClass()
                        .getResourceAsStream(
                                path);

        if (input == null) {

            System.out.println(
                    "WATERMARK NOT FOUND : "
                            + path);

            return null;
        }

        BufferedImage original =
                ImageIO.read(input);

        if (original == null) {

            return null;
        }

        int width =
                original.getWidth();

        int height =
                original.getHeight();

        BufferedImage transparent =
                new BufferedImage(
                        width,
                        height,
                        BufferedImage.TYPE_INT_ARGB);

        for (int y = 0;
             y < height;
             y++) {

            for (int x = 0;
                 x < width;
                 x++) {

                int rgb =
                        original.getRGB(
                                x,
                                y);

                int red =
                        (rgb >> 16)
                                & 0xff;

                int green =
                        (rgb >> 8)
                                & 0xff;

                int blue =
                        rgb & 0xff;

                boolean white =
                        red > 235
                                && green > 235
                                && blue > 235;

                int alpha;

                if (white) {

                    alpha = 0;

                } else {

                    // Very faint watermark
                    alpha = 22;
                }

                int newRgb =
                        (alpha << 24)
                                | (red << 16)
                                | (green << 8)
                                | blue;

                transparent.setRGB(
                        x,
                        y,
                        newRgb);
            }
        }

        ByteArrayOutputStream output =
                new ByteArrayOutputStream();

        ImageIO.write(
                transparent,
                "png",
                output);

        return Image.getInstance(
                output.toByteArray());

    } catch (Exception e) {

        e.printStackTrace();

        return null;
    }
}

    // INFORMATION CELL
    // =========================================================
    private PdfPCell informationCell() {

        PdfPCell cell =
                new PdfPCell();

        cell.setBorderColor(
                new Color(
                        220,
                        214,
                        202));

        cell.setBorderWidth(
                0.5f);

        cell.setBackgroundColor(
                new Color(
                        249,
                        247,
                        241));

        cell.setPadding(7);

        return cell;
    }


    // =========================================================
    // RIGHT INFORMATION
    // =========================================================

    private Paragraph rightInfo(
            String label,
            String value,
            Font labelFont,
            Font valueFont) {

        Paragraph paragraph =
                new Paragraph();

        paragraph.setAlignment(
                Element.ALIGN_RIGHT);

        paragraph.add(
                new Phrase(
                        label,
                        labelFont));

        paragraph.add(
                new Phrase(
                        value,
                        valueFont));

        return paragraph;
    }


    // =========================================================
    // HEADER CELL
    // =========================================================

    private void headerCell(
            PdfPTable table,
            String text,
            Font font,
            Color background) {

        PdfPCell cell =
                new PdfPCell(
                        new Phrase(
                                text,
                                font));

        cell.setBackgroundColor(
                background);

        cell.setBorder(
                Rectangle.NO_BORDER);

        cell.setPadding(7);

        cell.setHorizontalAlignment(
                Element.ALIGN_CENTER);

        table.addCell(cell);
    }


    // =========================================================
    // PRODUCT CELL
    // =========================================================

    private void productCell(
            PdfPTable table,
            String text,
            Font font,
            int alignment) {

        PdfPCell cell =
                new PdfPCell(
                        new Phrase(
                                text,
                                font));

        cell.setBorderColor(
                new Color(
                        220,
                        214,
                        202));

        cell.setBorderWidth(
                0.5f);

        cell.setPadding(6);

        cell.setHorizontalAlignment(
                alignment);

        table.addCell(cell);
    }


    // =========================================================
    // SUMMARY ROW
    // =========================================================

    private void summaryRow(
            PdfPTable table,
            String label,
            String value,
            Font font) {

        PdfPCell left =
                new PdfPCell(
                        new Phrase(
                                label,
                                font));

        left.setBorder(
                Rectangle.NO_BORDER);

        left.setPadding(3);


        PdfPCell right =
                new PdfPCell(
                        new Phrase(
                                value,
                                font));

        right.setBorder(
                Rectangle.NO_BORDER);

        right.setPadding(3);

        right.setHorizontalAlignment(
                Element.ALIGN_RIGHT);


        table.addCell(left);

        table.addCell(right);
    }


    // =========================================================
    // DIVIDER
    // =========================================================

    private Paragraph divider(
            Color color) {

        Paragraph line =
                new Paragraph(
                        "────────────────────────────────────────────────",
                        new Font(
                                Font.HELVETICA,
                                3,
                                Font.NORMAL,
                                color));

        line.setSpacingBefore(4);

        line.setSpacingAfter(4);

        return line;
    }


    // =========================================================
    // SAFE VALUE
    // =========================================================

    private String safe(
            Object value) {

        if (value == null) {

            return "";
        }

        return String.valueOf(value);
    }
}