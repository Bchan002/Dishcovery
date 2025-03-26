import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { RecipeStore } from '../../../componentStore/RecipeStore';
import { RecipeService } from '../../../../service/RecipeService';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Observable, Subscription } from 'rxjs';
import { Recipe } from '../../../model/models';

@Component({
  selector: 'app-search-recipe',
  standalone: false,
  templateUrl: './search-recipe.component.html',
  styleUrl: './search-recipe.component.css'
})
export class SearchRecipeComponent implements OnInit, OnDestroy{

      // Inject Routing
  private router = inject(Router)
  // Inject RecipeStore
  private recipeStore = inject(RecipeStore)
  private recipeSvc= inject(RecipeService)
  private fb = inject(FormBuilder)

  protected recipeForm!: FormGroup

  //Subscribe
  protected recipeSub!: Subscription

  //Variable 
  protected popularRecipe$!: Observable<Recipe[]>

  sidenavOpened = false; // Track sidenav state

  // Toggle sidenav open/closed
  toggleSidenav(): void {
    this.sidenavOpened = !this.sidenavOpened;
  }

  ngOnInit(): void {

    this.recipeForm = this.createForm()

  }

  createForm(): FormGroup {

    return this.fb.group({
      query: this.fb.control<String>("", Validators.required)
    })
  }

  searchRecipe(): void {

    localStorage.removeItem("searchedRecipes")
    console.info("Your search query is ", this.recipeForm.controls["query"]?.value)
    

    this.recipeStore.fetchSearchedRecipes(this.recipeForm.controls["query"]?.value)
    //this.recipeSvc.fetchSearchedRecipe(this.recipeForm.controls["query"]?.value)

    this.recipeForm.reset()

   

    this.router.navigate(['/layout/searchResults'])
  }

  ngOnDestroy() : void {

  }

  
}
