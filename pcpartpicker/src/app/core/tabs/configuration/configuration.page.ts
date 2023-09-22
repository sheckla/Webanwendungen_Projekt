import { Component, OnInit } from '@angular/core';
import { IonicModule } from '@ionic/angular';
import { CommonModule } from '@angular/common';
import { ApiService } from 'src/app/services/api/api.service';
import { PartCreatorService } from 'src/app/services/part-creator/part-creator.service';

@Component({
  selector: 'app-configuration',
  templateUrl: 'configuration.page.html',
  styleUrls: ['configuration.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule],
})
export class ConfigurationPage implements OnInit {
  constructor(public api: ApiService, public partCreator: PartCreatorService) {}

  ngOnInit(): void {
    this.api.getUserMe('admin', 'admin').subscribe(() => {
      this.api.getPrivateConfigurations();
    });
  }
}
