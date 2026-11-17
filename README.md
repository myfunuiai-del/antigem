<div align="center">

<img src="docs/readme/icon.svg" alt="AntiGem app icon" width="108">

# AntiGem (Custom Edition)

*A full coding environment and multi-agent IDE, right in your pocket.*

[![Kotlin](https://img.shields.io/badge/Kotlin-Jetpack%20Compose-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)](https://developer.android.com/jetpack/compose)
[![Go](https://img.shields.io/badge/Bridge-Go-00ADD8?style=for-the-badge&logo=go&logoColor=white)](https://go.dev)
[![License](https://img.shields.io/badge/License-GPLv3-blue?style=for-the-badge)](LICENSE)
[![GitHub](https://img.shields.io/badge/Fork-myfunuiai--del-black?style=for-the-badge&logo=github)](https://github.com/myfunuiai-del/antigem)

<p align="center">
  <a href="README_IN_DETAILS.md"><b>📖 Read Complete Detailed Documentation</b></a> •
  <a href="https://github.com/myfunuiai-del/antigem/releases"><b>⬇️ Downloads</b></a> •
  <a href="#-quick-upstream-sync"><b>🔄 Upstream Sync</b></a>
</p>

<img src="docs/readme/hero.webp" alt="AntiGem Hero" width="95%" style="border-radius: 12px; box-shadow: 0 4px 20px rgba(0,0,0,0.15);">

</div>

---

## ⚡ What is AntiGem?

**AntiGem** brings a complete desktop-grade AI development environment directly to your Android device:
- 🤖 **Multi-Agent Workspace:** Run **Antigravity**, **Claude Code**, **OpenCode**, and **Codex** side by side.
- 💻 **Real Linux Terminal:** Full Termux/proot environment for running Node.js, Python, Git, Go, and local build tools.
- 📝 **Advanced Code Editor:** Multi-tab file tree, syntax highlighting, and instant diff previews.
- 🌐 **Agent-Controlled Browser:** Built-in Chromium-based browser for live previews, testing, and web automation.
- 🔄 **Custom Dev Sync & Upstream Monitor:** Track both upstream sources (`Santoshkurmi` and `Aura`) in real time.

---

## 📸 Screenshots Showcase

<table>
  <tr>
    <td align="center" width="25%">
      <img src="docs/readme/screens/agent_picker.webp" width="180"><br>
      <b>Multi-Agent Picker</b>
    </td>
    <td align="center" width="25%">
      <img src="docs/readme/screens/chat_agent_working.webp" width="180"><br>
      <b>AI Live Execution</b>
    </td>
    <td align="center" width="25%">
      <img src="docs/readme/screens/code_editor.webp" width="180"><br>
      <b>Mobile Code Editor</b>
    </td>
    <td align="center" width="25%">
      <img src="docs/readme/screens/terminal_fastfetch.webp" width="180"><br>
      <b>Termux Linux Terminal</b>
    </td>
  </tr>
  <tr>
    <td align="center">
      <img src="docs/readme/screens/browser_live_preview.webp" width="180"><br>
      <b>Live Browser Preview</b>
    </td>
    <td align="center">
      <img src="docs/readme/screens/themes_gallery.webp" width="180"><br>
      <b>Theme Customizer</b>
    </td>
    <td align="center">
      <img src="docs/readme/screens/settings_main.webp" width="180"><br>
      <b>Preferences & MCP</b>
    </td>
    <td align="center">
      <img src="docs/readme/screens/model_picker_usage.webp" width="180"><br>
      <b>Quota & Usage Tracker</b>
    </td>
  </tr>
</table>

---

## 🔄 Quick Upstream Sync

Keep track of updates from both upstream repositories easily from your terminal:

```bash
# 1. Check updates from Official AntiGem (Santoshkurmi)
git fetch upstream && git log master..upstream/master --oneline

# 2. Check updates from Aura-NextGen-2 (Akash420421)
git fetch aura && git log upstream-aura..aura/master --oneline
```

> **In-App Upstream Monitor:** You can also track and review live git commits inside the app via **Settings → About & Info → Upstream Sync & Monitor**.

---

## 📚 Complete Guide & Documentation

Looking for the exhaustive setup instructions, architecture breakdown, bridge configs, or CLI usage?

👉 **[Click here to view README in Details (Full Documentation)](README_IN_DETAILS.md)**

---

<div align="center">
  <sub>Maintained & Custom Fork by <a href="https://github.com/myfunuiai-del">@myfunuiai-del</a> • Based on AntiGem by <a href="https://github.com/Santoshkurmi">Santoshkurmi</a></sub>
</div>
