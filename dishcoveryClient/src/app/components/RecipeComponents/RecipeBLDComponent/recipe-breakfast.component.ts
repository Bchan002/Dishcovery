import { Component, inject } from '@angular/core';
import { RecipeStore } from '../../../componentStore/RecipeStore';
import { Recipe } from '../../../model/models';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-recipe-breakfast',
  standalone: false,
  templateUrl: './recipe-breakfast.component.html',
  styleUrl: './recipe-breakfast.component.css'
})
export class RecipeBreakfastComponent {


     //Set this as the breakfast section 
     private state:string = "breakfast"

     //Inject store 
     private recipeStore = inject(RecipeStore)

     //Variable 
     protected breakfastRecipe$!: Observable<Recipe[]>

     ngOnInit():void{
        
      //this.recipeStore.filterRecipes(this.state)

      this.breakfastRecipe$ = this.recipeStore.getFilterRecipe(this.state)
        
     }
}
