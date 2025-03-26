import { Component, inject } from '@angular/core';
import { RecipeStore } from '../../../componentStore/RecipeStore';
import { Recipe } from '../../../model/models';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-recipe-dinner',
  standalone: false,
  templateUrl: './recipe-dinner.component.html',
  styleUrl: './recipe-dinner.component.css'
})
export class RecipeDinnerComponent {

         //Set this as the breakfast section 
     private state: string = "dinner"
    
     //Inject store 
     private recipeStore = inject(RecipeStore)
   
     //Variable 
     protected dinnerRecipe$!: Observable<Recipe[]>
   
     ngOnInit(): void {
   
       //this.recipeStore.filterRecipes(this.state)
   
       this.dinnerRecipe$ = this.recipeStore.getFilterRecipe(this.state)
   
     }

}
