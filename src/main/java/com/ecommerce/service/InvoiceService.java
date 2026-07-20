package com.ecommerce.service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.entity.OrderItem;
import com.ecommerce.entity.Orders;
import com.ecommerce.repository.OrderItemRepository;
import com.ecommerce.repository.OrderRepository;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

@Service
public class InvoiceService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    public byte[] generateInvoice(Long orderId)
            throws DocumentException, IOException {

        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order Not Found"));

        List<OrderItem> items = orderItemRepository.findByOrder(order);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Document document = new Document();

        PdfWriter.getInstance(document, outputStream);

        document.open();

        Font titleFont = new Font(Font.HELVETICA, 22, Font.BOLD, Color.BLUE);
        Font headingFont = new Font(Font.HELVETICA, 14, Font.BOLD);
        Font normalFont = new Font(Font.HELVETICA, 12);

        Paragraph title = new Paragraph("SRIG", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        Paragraph subtitle = new Paragraph("Own Your Style", headingFont);
        subtitle.setAlignment(Element.ALIGN_CENTER);
        document.add(subtitle);

        document.add(new Paragraph(" "));

        document.add(new Paragraph("Invoice No : INV-" + order.getId(), normalFont));
        document.add(new Paragraph("Customer : " + order.getCustomerName(), normalFont));
        document.add(new Paragraph("Mobile : " + order.getMobile(), normalFont));
        document.add(new Paragraph("Email : " + order.getEmail(), normalFont));
        document.add(new Paragraph("Address : " + order.getAddress(), normalFont));
        document.add(new Paragraph("Status : " + order.getStatus(), normalFont));

        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setWidths(new float[] {4, 2, 2, 2});

        table.addCell(new PdfPCell(new Phrase("Product")));
        table.addCell(new PdfPCell(new Phrase("Quantity")));
        table.addCell(new PdfPCell(new Phrase("Price")));
        table.addCell(new PdfPCell(new Phrase("Total")));

        for (OrderItem item : items) {

            table.addCell(item.getProduct().getName());

            table.addCell(String.valueOf(item.getQuantity()));

            table.addCell("Rs. " + item.getPrice());

            table.addCell("Rs. " +
                    (item.getPrice() * item.getQuantity()));
        }

        document.add(table);

        document.add(new Paragraph(" "));

        Paragraph total = new Paragraph(
                "Grand Total : Rs. " + order.getTotalAmount(),
                headingFont);

        total.setAlignment(Element.ALIGN_RIGHT);

        document.add(total);

        document.add(new Paragraph(" "));

        document.add(new Paragraph(
                "Thank you for shopping with SRIG - Own Your Style",
                headingFont));

        document.close();

        return outputStream.toByteArray();
    }
}