import { Component, OnInit, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-products',
  templateUrl: './products.html',
  styleUrl: './products.css'
})
export class Products implements OnInit {
  products = signal<any[]>([]);
  error = signal('');

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.http.get<any>(`${environment.gatewayUrl}/INVENTORY-SERVICE/products`).subscribe({
      next: data => this.products.set(data._embedded.products),
      error: err => this.error.set(err.message)
    });
  }
}
