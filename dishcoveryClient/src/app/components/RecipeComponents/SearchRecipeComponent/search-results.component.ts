import { ChangeDetectorRef, Component, inject, OnDestroy } from '@angular/core';
import { RecipeStore } from '../../../componentStore/RecipeStore';
import { Observable, of, delay } from 'rxjs';

@Component({
  selector: 'app-search-results',
  standalone: false,
  templateUrl: './search-results.component.html',
  styleUrl: './search-results.component.css'
})
export class SearchResultsComponent implements OnDestroy {

  private recipeStore = inject(RecipeStore)
  protected searchedRecipe$!: Observable<any[]>;
  protected errorMessage$!: Observable<string>;
  isLoading: boolean = false;
  private cdr = inject(ChangeDetectorRef);

  ngOnInit(): void {

    this.isLoading = true
    console.info("isloading ", this.isLoading)
    this.cdr.detectChanges();
    // Let Angular render the spinner first before loading
    setTimeout(() => {
      this.loadLatestRecipes();
    },0);


  }

  loadLatestRecipes() {
    this.recipeStore.clearSearchedRecipes();
  
    this.searchedRecipe$ = this.recipeStore.getSearchedRecipes;
    this.errorMessage$ = this.recipeStore.getErrorMessage;
  
    this.searchedRecipe$.subscribe(results => {
      this.isLoading = false;
      this.cdr.detectChanges(); // force view update again
    });
  }
  

  ngOnDestroy(): void {
    this.recipeStore.clearSearchedRecipes()
  }



}
