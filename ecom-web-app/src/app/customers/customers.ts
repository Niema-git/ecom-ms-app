import { Component, OnInit, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-customers',
  templateUrl: './customers.html',
  styleUrl: './customers.css'
})
export class Customers implements OnInit {
  customers = signal<any[]>([]);
  error = signal('');

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.http.get<any>(`${environment.gatewayUrl}/CUSTOMER-SERVICE/customers`).subscribe({
      next: data => this.customers.set(data._embedded.customers),
      error: err => this.error.set(err.message)
    });
  }
}
