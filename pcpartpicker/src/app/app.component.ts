import { Component, EnvironmentInjector, inject } from '@angular/core';
import { IonicModule } from '@ionic/angular';
import { CommonModule } from '@angular/common';
import { NavigationbarComponent } from "./core/components/navigationbar/navigationbar.component";
import { UserService } from './services/user/user.service';
import { ApiService } from './services/api/api.service';

@Component({
    selector: 'app-root',
    templateUrl: 'app.component.html',
    styleUrls: ['app.component.scss'],
    standalone: true,
    imports: [IonicModule, CommonModule, NavigationbarComponent]
})
export class AppComponent {
  public environmentInjector = inject(EnvironmentInjector);

  constructor(private user: UserService, private api: ApiService) {
    this.user.login('admin', 'admin').subscribe((data: any) => {
      console.log('login happened');
      this.api.getPrivateConfigurations();
      this.api.getAllParts()?.subscribe((data: any) => {});
    });
  }
}
