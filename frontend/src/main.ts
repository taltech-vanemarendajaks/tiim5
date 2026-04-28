import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { App } from './app/app';
import { OpenAPI } from '@/client';

OpenAPI.BASE = 'http://localhost:8080';

bootstrapApplication(App, appConfig).catch((err) => console.error(err));
