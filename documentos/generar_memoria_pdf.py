from pathlib import Path
import re

from reportlab.lib import colors
from reportlab.lib.enums import TA_CENTER, TA_LEFT
from reportlab.lib.pagesizes import A4
from reportlab.lib.styles import ParagraphStyle, getSampleStyleSheet
from reportlab.lib.units import cm
from reportlab.platypus import (
    BaseDocTemplate,
    Frame,
    PageBreak,
    PageTemplate,
    Paragraph,
    Preformatted,
    Spacer,
    Table,
    TableStyle,
    Flowable,
)
from xml.sax.saxutils import escape


ROOT = Path(__file__).resolve().parent
SOURCE = ROOT / "MEMORIA.md"
TARGETS = [
    ROOT / "MEMORIA.pdf",
    ROOT / "ENTREGA" / "MEMORIA.pdf",
]


class MermaidDiagram(Flowable):
    def __init__(self, kind: str, width: float = 450):
        super().__init__()
        self.kind = kind
        self.width = width
        self.height = 250 if kind == "class" else 170

    def wrap(self, avail_width, avail_height):
        self.width = min(self.width, avail_width)
        return self.width, self.height

    def draw_box(self, canvas, x, y, w, h, title, lines=None):
        canvas.setStrokeColor(colors.HexColor("#334155"))
        canvas.setFillColor(colors.HexColor("#f8fafc"))
        canvas.roundRect(x, y, w, h, 4, stroke=1, fill=1)
        canvas.setFillColor(colors.HexColor("#111827"))
        canvas.setFont("Helvetica-Bold", 8.5)
        canvas.drawCentredString(x + w / 2, y + h - 12, title)
        if lines:
            canvas.setFont("Helvetica", 6.8)
            yy = y + h - 24
            for line in lines[:4]:
                canvas.drawString(x + 5, yy, line)
                yy -= 8

    def line(self, canvas, x1, y1, x2, y2):
        canvas.setStrokeColor(colors.HexColor("#64748b"))
        canvas.setLineWidth(0.8)
        canvas.line(x1, y1, x2, y2)

    def draw(self):
        c = self.canv
        if self.kind == "graph":
            self.draw_graph(c)
        else:
            self.draw_class(c)

    def draw_graph(self, c):
        x0 = 18
        y0 = 20
        w = 130
        h = 36
        gap = 24
        c.setFont("Helvetica-Bold", 10)
        c.setFillColor(colors.HexColor("#111827"))
        c.drawString(x0, self.height - 18, "Grafo del mapa")
        self.draw_box(c, x0, y0 + 82, w, h, "Cueva facil", ["Criptas de Marfil"])
        self.draw_box(c, x0 + w + gap, y0 + 82, w, h, "Cueva media", ["Paramo Putrefacto"])
        self.draw_box(c, x0 + 2 * (w + gap), y0 + 82, w, h, "Cueva dificil", ["Abismo de Malakor"])
        self.draw_box(c, x0 + 2 * (w + gap), y0 + 18, w, h, "Boss final", ["Derrota y salida"])
        self.draw_box(c, x0, y0 + 18, w, h, "Llave media")
        self.draw_box(c, x0 + w + gap, y0 + 18, w, h, "Llave final")
        self.line(c, x0 + w, y0 + 100, x0 + w + gap, y0 + 100)
        self.line(c, x0 + 2 * w + gap, y0 + 100, x0 + 2 * (w + gap), y0 + 100)
        self.line(c, x0 + 2 * (w + gap) + w / 2, y0 + 82, x0 + 2 * (w + gap) + w / 2, y0 + 54)
        self.line(c, x0 + w / 2, y0 + 82, x0 + w / 2, y0 + 54)
        self.line(c, x0 + w + gap + w / 2, y0 + 82, x0 + w + gap + w / 2, y0 + 54)

    def draw_class(self, c):
        c.setFont("Helvetica-Bold", 10)
        c.setFillColor(colors.HexColor("#111827"))
        c.drawString(18, self.height - 18, "Modelo orientado a objetos")
        w = 94
        h = 50
        self.draw_box(c, 20, 170, w, h, "Partida", ["moverJugador", "atacar", "guardar"])
        self.draw_box(c, 178, 170, w, h, "Mazmorra", ["grafoCuevas", "cuevaActual"])
        self.draw_box(c, 336, 170, w, h, "Cueva", ["matriz", "caminoMinimo"])
        self.draw_box(c, 20, 86, w, h, "Jugador", ["inventario", "equipo"])
        self.draw_box(c, 178, 86, w, h, "Personaje", ["vida", "ataque", "defensa"])
        self.draw_box(c, 336, 86, w, h, "Enemigo", ["tipo", "movimiento"])
        self.draw_box(c, 20, 18, w, h, "Objeto", ["id", "nombre"])
        self.draw_box(c, 178, 18, w, h, "Arma", ["bonusAtaque"])
        self.draw_box(c, 336, 18, w, h, "Llave y Pocion", ["uso de inventario"])
        self.line(c, 114, 195, 178, 195)
        self.line(c, 272, 195, 336, 195)
        self.line(c, 67, 170, 67, 136)
        self.line(c, 225, 136, 225, 170)
        self.line(c, 272, 111, 336, 111)
        self.line(c, 67, 86, 67, 68)
        self.line(c, 114, 43, 178, 43)
        self.line(c, 272, 43, 336, 43)


def clean_inline(text: str) -> str:
    text = re.sub(r"`([^`]+)`", r"<font name='Courier'>\1</font>", text)
    text = re.sub(r"\*\*([^*]+)\*\*", r"<b>\1</b>", text)
    return text


def on_page(canvas, doc):
    canvas.saveState()
    canvas.setFont("Helvetica", 8)
    canvas.setFillColor(colors.HexColor("#666666"))
    canvas.drawRightString(A4[0] - 2 * cm, 1.2 * cm, f"Pagina {doc.page}")
    canvas.restoreState()


def make_styles():
    styles = getSampleStyleSheet()
    return {
        "title": ParagraphStyle(
            "title",
            parent=styles["Title"],
            fontName="Helvetica-Bold",
            fontSize=28,
            leading=34,
            alignment=TA_CENTER,
            textColor=colors.HexColor("#1f2937"),
            spaceAfter=18,
        ),
        "subtitle": ParagraphStyle(
            "subtitle",
            parent=styles["Heading2"],
            fontName="Helvetica-Bold",
            fontSize=16,
            leading=21,
            alignment=TA_CENTER,
            textColor=colors.HexColor("#374151"),
            spaceAfter=18,
        ),
        "h1": ParagraphStyle(
            "h1",
            parent=styles["Heading1"],
            fontName="Helvetica-Bold",
            fontSize=18,
            leading=23,
            textColor=colors.HexColor("#111827"),
            spaceBefore=8,
            spaceAfter=10,
        ),
        "h2": ParagraphStyle(
            "h2",
            parent=styles["Heading2"],
            fontName="Helvetica-Bold",
            fontSize=14,
            leading=18,
            textColor=colors.HexColor("#1f2937"),
            spaceBefore=8,
            spaceAfter=8,
        ),
        "body": ParagraphStyle(
            "body",
            parent=styles["BodyText"],
            fontName="Helvetica",
            fontSize=10.3,
            leading=14.2,
            alignment=TA_LEFT,
            spaceAfter=6,
        ),
        "bullet": ParagraphStyle(
            "bullet",
            parent=styles["BodyText"],
            fontName="Helvetica",
            fontSize=10.1,
            leading=13.8,
            leftIndent=14,
            firstLineIndent=-8,
            spaceAfter=4,
        ),
        "code": ParagraphStyle(
            "code",
            parent=styles["Code"],
            fontName="Courier",
            fontSize=7.2,
            leading=9,
            leftIndent=6,
            rightIndent=6,
            backColor=colors.HexColor("#f3f4f6"),
            borderColor=colors.HexColor("#d1d5db"),
            borderWidth=0.5,
            borderPadding=6,
            spaceBefore=6,
            spaceAfter=8,
        ),
    }


def split_table_row(line: str):
    cells = [cell.strip() for cell in line.strip().strip("|").split("|")]
    return cells


def build_story(markdown: str):
    styles = make_styles()
    story = []
    lines = markdown.splitlines()
    in_code = False
    code_lines = []
    table_lines = []
    paragraph = []
    first_h1 = True

    def flush_paragraph():
        nonlocal paragraph
        if paragraph:
            text = " ".join(part.strip() for part in paragraph).strip()
            story.append(Paragraph(clean_inline(escape(text)), styles["body"]))
            paragraph = []

    def flush_table():
        nonlocal table_lines
        if not table_lines:
            return
        rows = [split_table_row(row) for row in table_lines if not set(row.replace("|", "").strip()) <= {"-"}]
        if rows:
            table = Table(rows, repeatRows=1, hAlign="LEFT")
            table.setStyle(
                TableStyle(
                    [
                        ("BACKGROUND", (0, 0), (-1, 0), colors.HexColor("#e5e7eb")),
                        ("TEXTCOLOR", (0, 0), (-1, 0), colors.HexColor("#111827")),
                        ("FONTNAME", (0, 0), (-1, 0), "Helvetica-Bold"),
                        ("FONTNAME", (0, 1), (-1, -1), "Helvetica"),
                        ("FONTSIZE", (0, 0), (-1, -1), 7.4),
                        ("LEADING", (0, 0), (-1, -1), 9),
                        ("GRID", (0, 0), (-1, -1), 0.25, colors.HexColor("#cbd5e1")),
                        ("VALIGN", (0, 0), (-1, -1), "TOP"),
                        ("LEFTPADDING", (0, 0), (-1, -1), 4),
                        ("RIGHTPADDING", (0, 0), (-1, -1), 4),
                    ]
                )
            )
            story.append(table)
            story.append(Spacer(1, 8))
        table_lines = []

    for raw in lines:
        line = raw.rstrip()

        if line.startswith("```"):
            flush_paragraph()
            flush_table()
            if not in_code:
                in_code = True
                code_lines = []
                code_lang = line[3:].strip().lower()
            else:
                if code_lang == "mermaid":
                    body = "\n".join(code_lines)
                    kind = "class" if body.strip().startswith("classDiagram") else "graph"
                    story.append(MermaidDiagram(kind))
                    story.append(Spacer(1, 8))
                else:
                    story.append(Preformatted("\n".join(code_lines), styles["code"]))
                in_code = False
            continue

        if in_code:
            code_lines.append(line)
            continue

        if not line.strip():
            flush_paragraph()
            flush_table()
            continue

        if line.strip() == "---":
            flush_paragraph()
            flush_table()
            story.append(PageBreak())
            first_h1 = False
            continue

        if line.startswith("# "):
            flush_paragraph()
            flush_table()
            text = escape(line[2:].strip())
            if first_h1:
                story.append(Spacer(1, 5 * cm))
                story.append(Paragraph(text, styles["title"]))
                first_h1 = False
            else:
                story.append(Paragraph(text, styles["h1"]))
            continue

        if line.startswith("## "):
            flush_paragraph()
            flush_table()
            text = escape(line[3:].strip())
            if not story or isinstance(story[-1], Spacer):
                story.append(Paragraph(text, styles["subtitle"]))
            else:
                story.append(Paragraph(text, styles["h2"]))
            continue

        if line.startswith("|"):
            flush_paragraph()
            table_lines.append(line)
            continue

        if line.startswith("- "):
            flush_paragraph()
            flush_table()
            text = clean_inline(escape(line[2:].strip()))
            story.append(Paragraph(f"- {text}", styles["bullet"]))
            continue

        if re.match(r"^\d+\. ", line):
            flush_paragraph()
            flush_table()
            text = clean_inline(escape(line.strip()))
            story.append(Paragraph(text, styles["bullet"]))
            continue

        paragraph.append(line)

    flush_paragraph()
    flush_table()
    return story


def render(target: Path):
    target.parent.mkdir(parents=True, exist_ok=True)
    doc = BaseDocTemplate(
        str(target),
        pagesize=A4,
        leftMargin=2.2 * cm,
        rightMargin=2.2 * cm,
        topMargin=2.0 * cm,
        bottomMargin=2.0 * cm,
        title="Escape de la Mazmorra - Memoria",
        author="Alvaro Martinez del Campo, Guillermo Salgado Malcuori, Hector Montero Plaza",
    )
    frame = Frame(doc.leftMargin, doc.bottomMargin, doc.width, doc.height, id="normal")
    doc.addPageTemplates([PageTemplate(id="main", frames=[frame], onPage=on_page)])
    story = build_story(SOURCE.read_text(encoding="utf-8"))
    doc.build(story)


def main():
    text = SOURCE.read_text(encoding="utf-8")
    if chr(63) in text:
        raise SystemExit("La memoria contiene signos de interrogacion.")
    for codepoint in [0x250c, 0x2510, 0x2514, 0x2518, 0x2502, 0x2500, 0x253c, 0x2794, 0x2192]:
        forbidden = chr(codepoint)
        if forbidden in text:
            raise SystemExit("Caracter grafico prohibido en la memoria.")
    for target in TARGETS:
        render(target)


if __name__ == "__main__":
    main()
