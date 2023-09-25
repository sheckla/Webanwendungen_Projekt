import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { IonicModule } from '@ionic/angular';
import { ApiService } from 'src/app/services/api/api.service';
import { PublicConfigurationItemComponent } from "../../components/public-configuration-item/public-configuration-item.component";

@Component({
    selector: 'app-public',
    templateUrl: 'public.page.html',
    styleUrls: ['public.page.scss'],
    standalone: true,
    imports: [CommonModule, IonicModule, PublicConfigurationItemComponent]
})
export class PublicPage implements OnInit {
  constructor(public api: ApiService) {}

  public ngOnInit(): void {
    this.api.getPublicConfigurations().subscribe((data: any) => {
      console.log(data);
    });
  }
}
