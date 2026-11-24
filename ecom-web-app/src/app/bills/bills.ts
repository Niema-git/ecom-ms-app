import { Component, OnInit, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { DatePipe } from '@angular/common';
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-bills',
  imports: [DatePipe],
  templateUrl: './bills.html',
  styleUrl: './bills.css'
})
export class Bills implements OnInit {
  bills = signal<any[]>([]);
  error = signal('');
  customerId = '';

  constructor(private http: HttpClient, private route: ActivatedRoute, private router: Router) {}

  ngOnInit(): void {
    this.customerId = this.route.snapshot.params['customerId'];
    this.http.get<any>(`${environment.gatewayUrl}/BILLING-SERVICE/bills/search/byCustomerId?customerId=${this.customerId}`)
      .subscribe({
        next: data => this.bills.set(data._embedded.bills),
        error: err => this.error.set(err.message)
      });
  }

  getDetails(b: any) {
    this.router.navigateByUrl(`/bill-details/${b.id}`);
  }
}
