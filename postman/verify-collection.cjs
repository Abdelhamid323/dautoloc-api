// Vérification statique uniquement : aucune requête HTTP n'est envoyée.
const fs = require('node:fs');
const path = require('node:path');
const vm = require('node:vm');
const assert = require('node:assert/strict');

const collection = JSON.parse(fs.readFileSync(path.join(__dirname, 'AutoLoc.postman_collection.json'), 'utf8'));
const environment = JSON.parse(fs.readFileSync(path.join(__dirname, 'AutoLoc-local.postman_environment.json'), 'utf8'));
assert.equal(collection.info.schema, 'https://schema.getpostman.com/json/collection/v2.1.0/collection.json');
assert.equal(environment.values.find(v => v.key === 'baseUrl').value, 'http://localhost:8081/api');
let requests = 0;
let scripts = 0;
let bodies = 0;
const methodsByResource = new Map();
function inspect(items) {
    for (const item of items) {
        if (item.item) { inspect(item.item); continue; }
        requests++;
        assert.ok(item.name);
        assert.ok(item.request.url.raw.startsWith('{{baseUrl}}/'));
        const resource = item.request.url.path[0];
        if (!methodsByResource.has(resource)) methodsByResource.set(resource, new Set());
        methodsByResource.get(resource).add(item.request.method);
        assert.ok(item.event.some(e => e.listen === 'test'));
        for (const event of item.event) {
            new vm.Script(event.script.exec.join('\n'), {filename: item.name + '/' + event.listen});
            scripts++;
        }
        if (item.request.body) {
            const replaced = item.request.body.raw.replace(/\{\{(\w+)\}\}/g, (_, key) => key === 'runToken' ? 'abcdef0123' : '1');
            JSON.parse(replaced);
            bodies++;
        }
    }
}
inspect(collection.item);
assert.equal(methodsByResource.size, 9);
for (const [resource, methods] of methodsByResource) {
    const expected = resource === 'paiements' ? ['GET', 'POST'] : ['GET', 'POST', 'PUT', 'DELETE'];
    for (const method of expected) assert.ok(methods.has(method), `${resource} : ${method} absent`);
}
console.log(`JSON valides ; ${requests} requêtes, ${scripts} scripts JavaScript et ${bodies} corps JSON vérifiés. Aucun appel HTTP exécuté.`);
