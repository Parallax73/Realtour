import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class RouterService {

   constructor(private router: Router) {}


  navigateToHome(): void {
    this.router.navigate(['']);
  }

  
  navigateToLogin(): void {
    this.router.navigate(['login']);
  }

  navigateToRegister(): void{
    this.router.navigate(['register'])
  }
}
