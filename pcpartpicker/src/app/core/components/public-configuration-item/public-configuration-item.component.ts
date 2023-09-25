import { CommonModule } from '@angular/common';
import { Component, Input, OnInit } from '@angular/core';
import { IonicModule } from '@ionic/angular';
import { ApiService } from 'src/app/services/api/api.service';

@Component({
  selector: 'app-public-configuration-item',
  templateUrl: './public-configuration-item.component.html',
  styleUrls: ['./public-configuration-item.component.scss'],
  standalone: true,
  imports: [CommonModule, IonicModule],
})
export class PublicConfigurationItemComponent implements OnInit {
  @Input() item: any;
  constructor(public api: ApiService) {}

  ngOnInit() {}
}
