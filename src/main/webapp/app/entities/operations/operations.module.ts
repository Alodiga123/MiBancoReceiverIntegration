import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { OperationsComponent } from './list/operations.component';
import { OperationsDetailComponent } from './detail/operations-detail.component';
import { OperationsUpdateComponent } from './update/operations-update.component';
import { OperationsDeleteDialogComponent } from './delete/operations-delete-dialog.component';
import { OperationsRoutingModule } from './route/operations-routing.module';

@NgModule({
  imports: [SharedModule, OperationsRoutingModule],
  declarations: [OperationsComponent, OperationsDetailComponent, OperationsUpdateComponent, OperationsDeleteDialogComponent],
})
export class OperationsModule {}
