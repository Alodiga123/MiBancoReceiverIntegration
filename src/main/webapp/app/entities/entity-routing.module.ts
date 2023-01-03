import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'operations',
        data: { pageTitle: 'myBancoReceiverIntegrationApp.operations.home.title' },
        loadChildren: () => import('./operations/operations.module').then(m => m.OperationsModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
