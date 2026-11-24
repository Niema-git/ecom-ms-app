import { Routes } from '@angular/router';
import { Products } from './products/products';
import { Customers } from './customers/customers';
import { Bills } from './bills/bills';
import { BillDetails } from './bill-details/bill-details';

export const routes: Routes = [
  { path: 'products', component: Products },
  { path: 'customers', component: Customers },
  { path: 'bills/:customerId', component: Bills },
  { path: 'bill-details/:billId', component: BillDetails },
  { path: '', redirectTo: 'products', pathMatch: 'full' }
];
