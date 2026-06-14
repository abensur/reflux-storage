#!/usr/bin/env bun
/**
 * SoundLab for Reflux Storage.
 *
 *   bun tools/soundlab.ts   ->  http://localhost:7777
 *   PORT=7778 bun tools/soundlab.ts
 *
 * Generates ElevenLabs candidates, auditions them in the browser, compares
 * against the current mod OGG, and installs the chosen version as mono OGG.
 */
import { $ } from "bun";
import { mkdirSync, existsSync, readFileSync, readdirSync, unlinkSync } from "node:fs";
import { join } from "node:path";

const ROOT = join(import.meta.dir, "..");
const MOD_ID = "reflux_storage";
const MOD_SOUNDS = join(ROOT, "src/main/resources/assets", MOD_ID, "sounds", MOD_ID);
const AUDITIONS = join(ROOT, "auditions");
const PORT = Number(process.env.PORT ?? "7777");

const SOUNDS: Record<string, { display: string; prompt: string; dur: number }> = {
  burp_weak: {
    display: "Weak Burp Launch",
    prompt: "Tiny comedic burp puff, fizzy little pressure release, weak upward launch, short clean game sound effect",
    dur: 0.8,
  },
  burp_medium: {
    display: "Medium Burp Launch",
    prompt: "Comedic pressurized burp launch, fizzy magical gas burst, punchy upward propulsion, short clean game sound effect",
    dur: 1.2,
  },
  burp_strong: {
    display: "Strong Burp Launch",
    prompt: "Huge comedic power burp eruption, pressurized magical gas cannon, strong upward launch impact, short clean game sound effect",
    dur: 1.5,
  },
  empty: {
    display: "Empty Reflux Wheeze",
    prompt: "Small disappointed empty bottle wheeze, dry fizzy sputter, comedic failed magic device, short clean game sound effect",
    dur: 0.9,
  },
};

function apiKey(): string {
  if (process.env.ELEVENLABS_API_KEY) return process.env.ELEVENLABS_API_KEY;
  const envFile = join(process.env.HOME!, ".claude/.env");
  if (existsSync(envFile)) {
    const match = readFileSync(envFile, "utf8").match(/^ELEVENLABS_API_KEY=["']?([^"'\n]+)/m);
    if (match) return match[1];
  }
  throw new Error("ELEVENLABS_API_KEY not found");
}

function candidates(name: string): string[] {
  const dir = join(AUDITIONS, name);
  if (!existsSync(dir)) return [];
  return readdirSync(dir)
    .filter(file => file.endsWith(".mp3"))
    .sort((left, right) => parseInt(right.slice(1)) - parseInt(left.slice(1)));
}

function nextVersion(name: string): number {
  return candidates(name).reduce((max, file) => Math.max(max, parseInt(file.slice(1))), 0) + 1;
}

const HTML = `<!doctype html>
<html lang="en"><head><meta charset="utf-8">
<title>Reflux Storage SoundLab</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<style>
  :root { color-scheme: dark; }
  body { font-family: system-ui, sans-serif; background:#121417; color:#f2f4f8; margin:0; padding:24px; }
  h1 { margin:0 0 4px; font-size:26px; letter-spacing:0; }
  .sub { color:#9aa4b2; margin-bottom:24px; }
  .grid { display:grid; grid-template-columns:repeat(auto-fill,minmax(420px,1fr)); gap:16px; }
  .card { background:#1b2027; border:1px solid #303946; border-radius:8px; padding:16px; }
  .card h2 { margin:0 0 2px; font-size:18px; letter-spacing:0; }
  .id { color:#8c98a8; font-size:12px; margin-bottom:10px; }
  textarea { width:100%; box-sizing:border-box; background:#11161d; color:#f2f4f8; border:1px solid #3a4656;
             border-radius:8px; padding:8px; font-size:13px; resize:vertical; min-height:72px; }
  .row { display:flex; gap:8px; align-items:center; margin-top:8px; flex-wrap:wrap; }
  label { font-size:12px; color:#9aa4b2; }
  input[type=number] { width:60px; background:#11161d; color:#f2f4f8; border:1px solid #3a4656; border-radius:6px; padding:4px; }
  button { background:#3d6f8e; color:#fff; border:0; border-radius:8px; padding:8px 14px; font-size:13px; cursor:pointer; }
  button:hover { background:#477fa3; }
  button:disabled { background:#4b5563; cursor:wait; }
  button.install { background:#2f7d52; }
  button.install:hover { background:#388f60; }
  .current { margin:10px 0 4px; }
  .current span { font-size:12px; color:#9aa4b2; }
  audio { width:100%; height:32px; margin-top:4px; }
  .cands { max-height:300px; overflow-y:auto; margin-top:8px; padding-right:4px; }
  .cand { background:#141922; border-radius:8px; padding:6px 8px; margin-bottom:6px; }
  .cand .row { justify-content:space-between; margin-top:0; }
  .cand b { font-size:12px; color:#c5cedb; }
  button.discard { background:#694046; padding:6px 10px; }
  button.discard:hover { background:#7a4a51; }
  .msg { font-size:12px; margin-top:8px; min-height:16px; }
  .msg.ok { color:#6fd28d; }
  .msg.err { color:#ee8585; }
</style></head><body>
<h1>Reflux Storage SoundLab</h1>
<div class="sub">Generate candidates, audition them, compare against the current mod sound, then install. In-game, press F3+T to reload resources.</div>
<div class="grid" id="grid"></div>
<script>
const SOUNDS = __SOUNDS__;
async function refresh() {
  const state = await (await fetch('/api/state')).json();
  const grid = document.getElementById('grid'); grid.innerHTML = '';
  for (const [name, meta] of Object.entries(SOUNDS)) {
    const card = document.createElement('div'); card.className = 'card';
    card.innerHTML = \`
      <h2>\${meta.display}</h2><div class="id">\${name}</div>
      <div class="current"><span>current mod sound:</span>
        <audio controls preload="none" src="/current/\${name}?t=\${Date.now()}"></audio></div>
      <textarea id="p-\${name}">\${meta.prompt}</textarea>
      <div class="row">
        <label>variants <input type="number" id="n-\${name}" value="3" min="1" max="8"></label>
        <label>duration(s) <input type="number" id="d-\${name}" value="\${meta.dur}" step="0.1" min="0.5" max="10"></label>
        <button onclick="gen('\${name}', this)">Generate</button>
        <div class="msg" id="m-\${name}"></div>
      </div>
      <div class="cands" id="c-\${name}"></div>\`;
    grid.appendChild(card);
    renderCands(name, state[name] || []);
  }
}
function renderCands(name, files) {
  const box = document.getElementById('c-' + name); box.innerHTML = '';
  for (const file of files) {
    const div = document.createElement('div'); div.className = 'cand';
    div.innerHTML = \`<div class="row"><b>\${file}</b><span>
      <button class="discard" title="Discard candidate" onclick="discard('\${name}','\${file}', this)">Delete</button>
      <button class="install" onclick="install('\${name}','\${file}', this)">Install</button></span></div>
      <audio controls preload="none" src="/audio/\${name}/\${file}"></audio>\`;
    box.appendChild(div);
  }
}
async function gen(name, btn) {
  const msg = document.getElementById('m-' + name);
  btn.disabled = true; msg.className = 'msg'; msg.textContent = 'generating...';
  try {
    const body = { name, prompt: document.getElementById('p-'+name).value,
      n: +document.getElementById('n-'+name).value, dur: +document.getElementById('d-'+name).value };
    const response = await fetch('/api/gen', { method:'POST', headers:{'Content-Type':'application/json'}, body: JSON.stringify(body) });
    const json = await response.json();
    if (!response.ok) throw new Error(json.error || response.status);
    renderCands(name, json.files);
    msg.className = 'msg ok'; msg.textContent = \`+\${body.n} candidates\`;
  } catch (error) { msg.className = 'msg err'; msg.textContent = 'error: ' + error.message; }
  btn.disabled = false;
}
async function discard(name, file, btn) {
  const msg = document.getElementById('m-' + name);
  btn.disabled = true;
  try {
    const response = await fetch('/api/discard', { method:'POST', headers:{'Content-Type':'application/json'},
      body: JSON.stringify({ name, file }) });
    const json = await response.json();
    if (!response.ok) throw new Error(json.error || response.status);
    btn.closest('.cand').remove();
    msg.className = 'msg'; msg.textContent = \`\${file} discarded\`;
  } catch (error) { msg.className = 'msg err'; msg.textContent = 'error: ' + error.message; btn.disabled = false; }
}
async function install(name, file, btn) {
  const msg = document.getElementById('m-' + name);
  btn.disabled = true;
  try {
    const response = await fetch('/api/install', { method:'POST', headers:{'Content-Type':'application/json'},
      body: JSON.stringify({ name, file }) });
    const json = await response.json();
    if (!response.ok) throw new Error(json.error || response.status);
    msg.className = 'msg ok'; msg.textContent = \`\${file} installed; press F3+T in-game\`;
    document.querySelector(\`audio[src^="/current/\${name}"]\`).src = \`/current/\${name}?t=\${Date.now()}\`;
  } catch (error) { msg.className = 'msg err'; msg.textContent = 'error: ' + error.message; }
  btn.disabled = false;
}
refresh();
</script></body></html>`;

Bun.serve({
  port: PORT,
  idleTimeout: 120,
  async fetch(req) {
    const url = new URL(req.url);
    const path = url.pathname;

    if (path === "/") {
      return new Response(HTML.replace("__SOUNDS__", JSON.stringify(SOUNDS)), {
        headers: { "Content-Type": "text/html; charset=utf-8" },
      });
    }

    if (path === "/favicon.ico") return new Response(null, { status: 204 });

    if (path === "/api/state") {
      const state: Record<string, string[]> = {};
      for (const name of Object.keys(SOUNDS)) state[name] = candidates(name);
      return Response.json(state);
    }

    if (path === "/api/gen" && req.method === "POST") {
      const { name, prompt, n = 3, dur = 2.0 } = await req.json();
      if (!SOUNDS[name]) return Response.json({ error: "invalid sound name" }, { status: 400 });
      if (!prompt?.trim()) return Response.json({ error: "empty prompt" }, { status: 400 });
      const dir = join(AUDITIONS, name);
      mkdirSync(dir, { recursive: true });
      const start = nextVersion(name);
      try {
        await Promise.all(Array.from({ length: Math.min(n, 8) }, async (_, index) => {
          const response = await fetch("https://api.elevenlabs.io/v1/sound-generation", {
            method: "POST",
            headers: { "xi-api-key": apiKey(), "Content-Type": "application/json" },
            body: JSON.stringify({ text: prompt, duration_seconds: dur, prompt_influence: 0.6 }),
          });
          if (!response.ok) throw new Error(`ElevenLabs HTTP ${response.status}: ${await response.text()}`);
          await Bun.write(join(dir, `v${start + index}.mp3`), await response.arrayBuffer());
        }));
      } catch (error: any) {
        return Response.json({ error: error.message }, { status: 502 });
      }
      return Response.json({ files: candidates(name) });
    }

    if (path === "/api/discard" && req.method === "POST") {
      const { name, file } = await req.json();
      if (!SOUNDS[name]) return Response.json({ error: "invalid sound name" }, { status: 400 });
      if (!/^v\d+\.mp3$/.test(file)) return Response.json({ error: "invalid file" }, { status: 400 });
      const target = join(AUDITIONS, name, file);
      if (!existsSync(target)) return Response.json({ error: "candidate does not exist" }, { status: 404 });
      unlinkSync(target);
      return Response.json({ ok: true });
    }

    if (path === "/api/install" && req.method === "POST") {
      const { name, file } = await req.json();
      if (!SOUNDS[name]) return Response.json({ error: "invalid sound name" }, { status: 400 });
      if (!/^v\d+\.mp3$/.test(file)) return Response.json({ error: "invalid file" }, { status: 400 });
      const src = join(AUDITIONS, name, file);
      if (!existsSync(src)) return Response.json({ error: "candidate does not exist" }, { status: 404 });
      mkdirSync(MOD_SOUNDS, { recursive: true });
      const dest = join(MOD_SOUNDS, `${name}.ogg`);
      await $`ffmpeg -y -loglevel error -i ${src} -af ${"loudnorm=I=-18:TP=-2:LRA=7"} -c:a libvorbis -q:a 4 -ar 44100 -ac 1 ${dest}`;
      return Response.json({ ok: true });
    }

    const audio = path.match(/^\/audio\/([a-z_]+)\/(v\d+\.mp3)$/);
    if (audio && SOUNDS[audio[1]]) {
      const file = Bun.file(join(AUDITIONS, audio[1], audio[2]));
      return (await file.exists()) ? new Response(file) : new Response("404", { status: 404 });
    }

    const current = path.match(/^\/current\/([a-z_]+)$/);
    if (current && SOUNDS[current[1]]) {
      const file = Bun.file(join(MOD_SOUNDS, `${current[1]}.ogg`));
      return (await file.exists()) ? new Response(file) : new Response("404", { status: 404 });
    }

    return new Response("404", { status: 404 });
  },
});

console.log(`SoundLab running at http://localhost:${PORT}`);
