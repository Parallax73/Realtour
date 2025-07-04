import { Component, inject } from '@angular/core';
import { MenuNavbarComponent } from "../../components/menu-navbar/menu-navbar.component";
import { ButtonModule } from 'primeng/button';
import { CarouselModule } from 'primeng/carousel';
import { RouterService } from '../../services/router/router.service';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'app-home-page',
  imports: [
    MenuNavbarComponent,
    ButtonModule,
    CarouselModule,
    TranslateModule
  ],
  templateUrl: './home-page.component.html',
  styleUrl: './home-page.component.scss'
})
export class HomePageComponent {
  routerService = inject(RouterService);

  images = [
    { src: 'assets/images/room-sample.png', alt: 'House 1' }
  ];
}
