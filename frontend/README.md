# Hello World — React + OpenShift

A minimal React app wired for CI/CD to OpenShift via GitHub Actions.

## Local development

```bash
npm install
npm start        # http://localhost:3000
```

## Project structure

```
.
├── public/                   # Static HTML shell
├── src/
│   ├── App.js / App.css      # Main component
│   └── index.js / index.css  # Entry point
├── openshift/
│   └── deployment.yaml       # Deployment, Service, Route
├── .github/workflows/
│   └── deploy.yml            # CI/CD pipeline
├── Dockerfile                # Multi-stage build (non-root nginx)
├── nginx.conf                # SPA routing + health check
└── package.json
```

## GitHub Secrets required

Set these in **Settings → Secrets and variables → Actions**:

| Secret | Description |
|---|---|
| `REGISTRY` | Container registry hostname (e.g. `quay.io`) |
| `REGISTRY_NS` | Image namespace/repository (e.g. `myorg/hello-world`) |
| `REGISTRY_USER` | Registry login username |
| `REGISTRY_PASSWORD` | Registry login password or token |
| `OPENSHIFT_URL` | API server URL (e.g. `https://api.mycluster.example.com:6443`) |
| `OPENSHIFT_TOKEN` | Service account token with `edit` role in the namespace |
| `OCP_NAMESPACE` | Target OpenShift project/namespace |

## OpenShift service account setup

```bash
# Create a dedicated service account for GitHub Actions
oc create sa github-actions -n <your-namespace>
oc policy add-role-to-user edit \
  system:serviceaccount:<your-namespace>:github-actions \
  -n <your-namespace>

# Get the token (OpenShift 4.11+)
oc create token github-actions -n <your-namespace> --duration=8760h
```

## Pipeline flow

```
push to main
  └─► test          (npm test)
        └─► build   (docker build → push to registry)
              └─► deploy (oc apply → rollout)
```

Pull requests run the `test` job only — no image push or deploy.
