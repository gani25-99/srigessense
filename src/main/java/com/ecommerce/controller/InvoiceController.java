package com.ecommerce.controller;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ecommerce.entity.OrderItem;
import com.ecommerce.entity.Orders;
import com.ecommerce.repository.OrderItemRepository;
import com.ecommerce.repository.OrderRepository;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class InvoiceController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @GetMapping("/invoice/{id}")
    public String invoice(@PathVariable Long id, Model model) {

        Orders order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order Not Found"));

        List<OrderItem> items = orderItemRepository.findByOrder(order);

        model.addAttribute("order", order);
        model.addAttribute("items", items);

        return "invoice";
    }

    @GetMapping("/invoice/pdf/{id}")
    public void downloadInvoice(@PathVariable Long id,
                                HttpServletResponse response)
            throws DocumentException, IOException {

        Orders order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order Not Found"));

        List<OrderItem> items = orderItemRepository.findByOrder(order);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition",
                "attachment; filename=Invoice_" + order.getId() + ".pdf");

        Document document = new Document();

        PdfWriter.getInstance(document, response.getOutputStream());

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
        document.add(new Paragraph("Address : " + order.getAddress(), normalFont));
        document.add(new Paragraph("Status : " + order.getStatus(), normalFont));

        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);

        PdfPCell cell;

        cell = new PdfPCell(new Phrase("Product"));
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Quantity"));
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Price"));
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Total"));
        table.addCell(cell);

        for (OrderItem item : items) {

            table.addCell(item.getProduct().getName());

            table.addCell(item.getQuantity().toString());

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
    }
}