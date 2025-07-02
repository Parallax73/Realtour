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

  navigateToUnits():void{
    this.router.navigate(['units'])
  }

  navigateToRealtors():void{
    this.router.navigate(['realtors'])
  }

  navigateToMap():void{
    this.router.navigate(['map'])
  }

  navigateToContact():void{
    this.router.navigate(['contact'])
  }

  navigateToChat():void{
    this.router.navigate(['chat'])
  }

  navigateToRealtor(id: string):void{
    this.router.navigate(['/realtor/'+id])
  }

  navigateToUnit(id: string):void{
    this.router.navigate(['/unit/'+id])
  }
}
