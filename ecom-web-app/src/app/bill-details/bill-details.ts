import { Component, OnInit, computed, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { DatePipe, DecimalPipe } from '@angular/common';
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-bill-details',
  imports: [DatePipe, DecimalPipe],
  templateUrl: './bill-details.html',
  styleUrl: './bill-details.css'
})
export class BillDetails implements OnInit {
  bill = signal<any>(null);
  error = signal('');
  total = computed(() =>
    (this.bill()?.productItems ?? []).reduce((s: number, pi: any) => s + pi.unitPrice * pi.quantity, 0));

  constructor(private http: HttpClient, private route: ActivatedRoute) {}

  ngOnInit(): void {
    const billId = this.route.snapshot.params['billId'];
    this.http.get<any>(`${environment.gatewayUrl}/BILLING-SERVICE/api/bills/${billId}`).subscribe({
      next: data => this.bill.set(data),
      error: err => this.error.set(err.message)
    });
  }
}
