import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { SidebarComponent } from '../sidebar/sidebar.component';

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [RouterOutlet, SidebarComponent],
  template: `
    <div class="flex h-screen overflow-hidden bg-bg-secondary">
      <app-sidebar />
      <main class="flex-1 overflow-y-auto bg-hero-gradient bg-bg-secondary">
        <div class="min-h-full">
          <router-outlet />
        </div>
      </main>
    </div>
  `,
})
export class LayoutComponent {}
