import { Component } from '@angular/core';
import { MenuNavbarComponent } from "../../components/menu-navbar/menu-navbar.component";
import { ButtonModule } from 'primeng/button';
import { CarouselModule } from 'primeng/carousel';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home-page',
  imports: [
    MenuNavbarComponent,
    ButtonModule,
    CarouselModule
  ],
  templateUrl: './home-page.component.html',
  styleUrl: './home-page.component.scss'
})
export class HomePageComponent {

  constructor(private router: Router) {}

  images = [
    { src: 'assets/images/room-sample.png', alt: 'House 1' },
   
    
  
  ];

}
