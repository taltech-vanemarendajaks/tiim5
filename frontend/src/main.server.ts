import { BootstrapContext, bootstrapApplication } from '@angular/platform-browser';
import { App } from './app/app';
import { config } from './app/app.config.server';
import { OpenAPI } from '@/client';

OpenAPI.BASE = process.env['BACKEND_URL'] ?? 'http://localhost:8080';

const bootstrap = (context: BootstrapContext) => bootstrapApplication(App, config, context);

export default bootstrap;
