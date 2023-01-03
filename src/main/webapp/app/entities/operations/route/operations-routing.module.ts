import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { OperationsComponent } from '../list/operations.component';
import { OperationsDetailComponent } from '../detail/operations-detail.component';
import { OperationsUpdateComponent } from '../update/operations-update.component';
import { OperationsRoutingResolveService } from './operations-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const operationsRoute: Routes = [
  {
    path: '',
    component: OperationsComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OperationsDetailComponent,
    resolve: {
      operations: OperationsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OperationsUpdateComponent,
    resolve: {
      operations: OperationsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OperationsUpdateComponent,
    resolve: {
      operations: OperationsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(operationsRoute)],
  exports: [RouterModule],
})
export class OperationsRoutingModule {}
