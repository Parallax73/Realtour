import { Component} from '@angular/core';
import { SideBarComponent } from "../../components/side-bar/side-bar.component";


@Component({
  selector: 'app-unit-page',
  imports: [SideBarComponent],
  templateUrl: './unit-page.component.html',
  styleUrl: './unit-page.component.scss'
})
export class UnitPageComponent {
}
