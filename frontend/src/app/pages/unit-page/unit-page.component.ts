import { Component } from '@angular/core';
import { MenuNavbarComponent } from "../../components/menu-navbar/menu-navbar.component";
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CheckboxModule } from 'primeng/checkbox';
import { ButtonModule } from 'primeng/button';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'app-unit-page',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ButtonModule,
    MenuNavbarComponent,
    CheckboxModule,
    TranslateModule
  ],
  templateUrl: './unit-page.component.html',
  styleUrl: './unit-page.component.scss'
})
export class UnitPageComponent {
  selectedCategories: any[] = [];
  selectedIndexGroup1: number = -1;
  selectedIndexGroup2: number = -1;
  apartment: any = null;
  house: any = null;

  group1 = [
    { id: 1, label: '1' },
    { id: 2, label: '2' },
    { id: 3, label: '3' },
    { id: 4, label: '4+' }
  ];

  group2 = [
    { id: 1, label: '1' },
    { id: 2, label: '2' },
    { id: 3, label: '3' },
    { id: 4, label: '4+' }
  ];

  categories: any[] = [
    { name: 'Gym', key: 'A' },
    { name: 'Garage', key: 'M' },
    { name: 'Pool', key: 'P' },
    { name: 'Elevator', key: 'R' }
  ];

  isSelected(group: number, index: number): boolean {
    return group === 1
      ? this.selectedIndexGroup1 === index
      : this.selectedIndexGroup2 === index;
  }

  onCheckboxChange(group: number, index: number) {
    if (group === 1) {
      this.selectedIndexGroup1 = this.selectedIndexGroup1 === index ? -1 : index;
    } else {
      this.selectedIndexGroup2 = this.selectedIndexGroup2 === index ? -1 : index;
    }
  }

  resetFilters() {
    this.apartment = null;
    this.house = null;
    this.selectedIndexGroup1 = -1;
    this.selectedIndexGroup2 = -1;
    this.selectedCategories = [];
  }
}
