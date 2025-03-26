import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { MaterialModule } from './material.module';
import { ReactiveFormsModule } from '@angular/forms';
import { GroceryService } from '../service/GroceryService';
import { provideFirebaseApp, initializeApp } from '@angular/fire/app';
import { provideMessaging, getMessaging } from '@angular/fire/messaging';
import { AngularFireModule } from '@angular/fire/compat';
import { AngularFireMessagingModule } from '@angular/fire/compat/messaging';
import { environment } from './environments/environment';
import { LoginComponentComponent } from './components/LoginComponent/login-component.component';
import { RouterModule, Routes } from '@angular/router';
import { UserService } from '../service/UserService';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { authInterceptor } from './utils/AuthInterceptor';
import { SignUpComponent } from './components/SignUpComponent/sign-up.component';
import { SignUpSuccessComponent } from './components/SignUpComponent/sign-up-success.component';
import { ForgotPasswordComponent } from './components/ForgotPasswordComponent/forgot-password.component';
import { MainLayoutComponent } from './components/MainLayoutComponent/main-layout.component';
import { isAuthenticated } from './utils/RouteGuards';
import { NavBarComponent } from './components/NavBarComponent/nav-bar.component';
import { DashboardComponent } from './components/DashboardComponent/dashboard.component';
import { NotificationService } from '../service/NotificationService';
import { RecipeService } from '../service/RecipeService';
import { PriceCompareService } from '../service/PriceComparisonService';
import { AiRecipeService } from '../service/AiRecipeService';
import { ExpiredItemService } from '../service/ExpiredItemService';
import { RecipeStore } from './componentStore/RecipeStore';
import { GroceryStore } from './componentStore/GroceryStore';
import { ComponentStore } from '@ngrx/component-store';
import { LayoutModule } from '@angular/cdk/layout';
import { AiRecipeModalComponent } from './components/AiRecipeModalComponent/ai-recipe-modal.component';
import { SearchRecipeComponent } from './components/RecipeComponents/SearchRecipeComponent/search-recipe.component';
import { SearchResultsComponent } from './components/RecipeComponents/SearchRecipeComponent/search-results.component';
import { SearchRecipeDetailsComponent } from './components/RecipeComponents/SearchRecipeComponent/search-recipe-details.component';
import { RecipeBreakfastComponent } from './components/RecipeComponents/RecipeBLDComponent/recipe-breakfast.component';
import { RecipeLunchComponent } from './components/RecipeComponents/RecipeBLDComponent/recipe-lunch.component';
import { RecipeDinnerComponent } from './components/RecipeComponents/RecipeBLDComponent/recipe-dinner.component';
import { RecipeDetailsComponent } from './components/RecipeComponents/RecipeBLDComponent/recipe-details.component';
import { SavedRecipesComponent } from './components/RecipeComponents/SavedRecipeComponent/saved-recipes.component';
import { SavedRecipesDetailsComponent } from './components/RecipeComponents/SavedRecipeComponent/saved-recipes-details.component';
import { ShoppingListService } from '../service/ShoppingListService';
import { AddGroceryComponent } from './components/HomeGroceryComponent/add-grocery.component';
import { SavedGroceryComponent } from './components/HomeGroceryComponent/saved-grocery.component';
import { ShoppingListComponent } from './components/ShoppingListComponent/shopping-list.component';
import { ComparePriceComponent } from './components/ComparePriceComponent/compare-price.component';
import { ComparePriceResultComponent } from './components/ComparePriceComponent/compare-price-result.component';
import { ChangePasswordComponent } from './components/change-password.component';



const appRoutes:Routes = [ 

  {path:'', component:LoginComponentComponent}, 
  {path:'signUp', component:SignUpComponent}, 
  {path:'signUpSuccess',component:SignUpSuccessComponent},
  {path:'forgotPassword',component:ForgotPasswordComponent},
  {path: 'layout', component:MainLayoutComponent, 
    canActivate: [isAuthenticated], 
    children: [
      {path: '', component:DashboardComponent},
      {path: 'dashboard',component:DashboardComponent},
      {path: 'searchRecipe',component:SearchRecipeComponent},
      {path: 'searchResults',component:SearchResultsComponent},
      {path: 'searchRecipeDetails/:id', component:SearchRecipeDetailsComponent},
      {path: 'recipeBreakfast', component:RecipeBreakfastComponent},
      {path: 'recipeLunch',component:RecipeLunchComponent},
      {path: 'recipeDinner', component:RecipeDinnerComponent},
      {path:'recipe/:id',component:RecipeDetailsComponent},
      {path:'savedRecipes',component:SavedRecipesComponent},
      {path: 'savedRecipeDetails/:id',component:SavedRecipesDetailsComponent},
      {path: 'addGrocery',component:AddGroceryComponent},
      {path: 'groceryStore', component:SavedGroceryComponent},
      {path: 'shoppingList',component:ShoppingListComponent},
      {path: 'comparePrice',component:ComparePriceComponent}, 
      {path: 'comparePriceResult',component:ComparePriceResultComponent},
      {path: 'aiRecipeModal', component:AiRecipeModalComponent}, 
      
    ]
    
    
  },





  {path:'**', redirectTo:'/',pathMatch:'full'}

]




@NgModule({
  declarations: [
    AppComponent,
    LoginComponentComponent,
    SignUpComponent,
    SignUpSuccessComponent,
    ForgotPasswordComponent,
    MainLayoutComponent,
    NavBarComponent,
    DashboardComponent,
    AiRecipeModalComponent,
    SearchRecipeComponent,
    SearchResultsComponent,
    SearchRecipeDetailsComponent,
    RecipeBreakfastComponent,
    RecipeLunchComponent,
    RecipeDinnerComponent,
    RecipeDetailsComponent,
    SavedRecipesComponent,
    SavedRecipesDetailsComponent,
    AddGroceryComponent,
    SavedGroceryComponent,
    ShoppingListComponent,
    ComparePriceComponent,
    ComparePriceResultComponent,
    ChangePasswordComponent,
  ],
  imports: [
    BrowserModule, MaterialModule,ReactiveFormsModule, LayoutModule,
    RouterModule.forRoot(appRoutes, { useHash: true }),
    AngularFireModule.initializeApp(environment), AngularFireMessagingModule

  ],
  providers: [GroceryService, UserService,NotificationService,RecipeService,PriceCompareService,AiRecipeService,ExpiredItemService,
    ShoppingListService,
    RecipeStore, GroceryStore, ComponentStore, 
    provideHttpClient(withInterceptors([authInterceptor])),
    provideFirebaseApp( () => initializeApp(environment)),  provideMessaging(() => getMessaging())
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
