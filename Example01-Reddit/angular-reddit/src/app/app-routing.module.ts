import { LoginComponent } from './auth/login/login.component';
import { TestComponent } from './test/test.component';
import { SignupComponent } from './auth/signup/signup.component';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

const routes: Routes = [
  
  { path: 'signup', component: SignupComponent },
  { path: 'login', component: LoginComponent },
  { path: '*', component: TestComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
