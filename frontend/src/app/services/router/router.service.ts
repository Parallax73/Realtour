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

  navigateToModels():void{
    this.router.navigate(['models'])
  }

  navigateToContact():void{
    this.router.navigate(['contact'])
  }

  navigateToEditor():void{
    this.router.navigate(['editor'])
  }

  navigateToCode():void{
     window.location.href = 'https://github.com/Parallax73/Realtour';
  }
}
