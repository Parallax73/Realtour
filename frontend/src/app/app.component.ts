import { Component, inject, OnInit, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { RouterOutlet } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { TranslateService } from '@ngx-translate/core';


@Component({
  selector: 'app-root',
  imports: [RouterOutlet,ButtonModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent implements OnInit{
  title = 'Realtour';
  
  isDarkMode = false;
  private readonly platformId = inject(PLATFORM_ID);
  translate = inject(TranslateService);


  private updateThemeClass(isDark: boolean): void {
    if (isPlatformBrowser(this.platformId)) {
      const element = document.documentElement;
      if (element) {
        if (isDark) {
          element.classList.add('my-app-dark');
        } else {
          element.classList.remove('my-app-dark');
        }
      }
    }
  }

  ngOnInit() {
    const lang = localStorage.getItem('lang') || 'en';
    this.translate.setDefaultLang('en');
    this.translate.use(lang);
    if (isPlatformBrowser(this.platformId)) {
      const savedTheme = localStorage.getItem('theme');
      if (savedTheme) {
        this.isDarkMode = savedTheme === 'dark';
        this.updateThemeClass(this.isDarkMode);
      } else {
        const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches;
        this.isDarkMode = prefersDark;
        this.updateThemeClass(this.isDarkMode);
      }
    }

}
}
