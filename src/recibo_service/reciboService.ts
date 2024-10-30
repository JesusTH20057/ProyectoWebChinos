// src/app/services/receipt.service.ts
import { Injectable } from '@angular/core';

interface ReceiptItem {
  id: number;
  nombre: string;
  precio: number;
  cantidad?: number; // Optional quantity, defaults to 1 if not provided
}

@Injectable({
  providedIn: 'root',
})
export class ReceiptService {
  generateReceipt(items: ReceiptItem[], total: number): void {
    const impuestos = (total * 0.16).toFixed(2); // 16% tax
    let receiptContent = `<?xml version="1.0" encoding="UTF-8"?>\n`;
    receiptContent += `<receipt>\n  <items>\n`;

    items.forEach(item => {
      receiptContent += `    <item>\n`;
      receiptContent += `      <nombre>${this.escapeXml(item.nombre)}</nombre>\n`;
      receiptContent += `      <precio>${item.precio}</precio>\n`;
      receiptContent += `      <cantidad>${item.cantidad || 1}</cantidad>\n`;
      receiptContent += `    </item>\n`;
    });

    receiptContent += `  </items>\n  <total>${total}</total>\n`;
    receiptContent += `  <impuestos>${impuestos}</impuestos>\n</receipt>`;

    this.downloadFile(receiptContent, 'receipt.xml', 'text/xml');
  }

  private downloadFile(content: string, fileName: string, mimeType: string): void {
    const blob = new Blob([content], { type: mimeType });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = fileName;
    document.body.appendChild(a);
    a.click();
    window.URL.revokeObjectURL(url);
    document.body.removeChild(a);
  }

  // Optional: Escape XML special characters to prevent malformed XML
  private escapeXml(unsafe: string): string {
    return unsafe.replace(/[<>&'"]/g, function (c) {
      switch (c) {
        case '<': return '&lt;';
        case '>': return '&gt;';
        case '&': return '&amp;';
        case '\'': return '&apos;';
        case '"': return '&quot;';
        default: return c;
      }
    });
  }
}
