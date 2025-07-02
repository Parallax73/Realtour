import { Component } from '@angular/core';
import { DrawerModule } from 'primeng/drawer';
import { ButtonModule } from 'primeng/button';
import { MenuModule } from 'primeng/menu'; 

@Component({
  selector: 'app-side-bar',
  imports: [
    DrawerModule,
    ButtonModule,
    MenuModule  
  ],
  templateUrl: './side-bar.component.html',
  styleUrl: './side-bar.component.scss'
})
export class SideBarComponent{
  isSideBarVisible = false;

 

  toggleSideBar(){
    this.isSideBarVisible = !this.isSideBarVisible;
  }
}