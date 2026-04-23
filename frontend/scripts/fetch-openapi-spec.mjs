import fs from 'node:fs';

const ROOT_DIRECTORY = `${import.meta.dirname}/..`;
const OPEN_API_DOCS_URL = process.env.OPEN_API_DOCS_URL ?? 'http://localhost:8080/api/docs';
const OUTPUT_FILE = 'openapi.json';
const OUTPUT_PATH = `${ROOT_DIRECTORY}/${OUTPUT_FILE}`;

console.log(`Fetching OpenAPI spec from ${OPEN_API_DOCS_URL}`);

const response = await fetch(OPEN_API_DOCS_URL);
const content = await response.json();

fs.writeFileSync(OUTPUT_PATH, JSON.stringify(content, null, 2));

console.log(`OpenAPI spec fetched and saved to ${OUTPUT_FILE}`);
