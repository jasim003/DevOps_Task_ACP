{{/*
Expand the name of the chart.
*/}}
{{- define "web-app-svc.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Create chart name and version as used by the chart label.
*/}}
{{- define "web-app-svc.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Common labels
*/}}
{{- define "web-app-svc.labels" -}}
app: {{ .Release.Name }}
helm.sh/chart: {{ include "web-app-svc.chart" . }}
{{ include "web-app-svc.selectorLabels" . }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}

{{/*
Selector labels
*/}}
{{- define "web-app-svc.selectorLabels" -}}
app.kubernetes.io/name: {{ include "web-app-svc.name" . }}
app.kubernetes.io/instance: {{ .Chart.Name }}
{{- end }}
