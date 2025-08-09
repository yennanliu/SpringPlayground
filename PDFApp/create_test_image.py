from PIL import Image, ImageDraw, ImageFont
import base64
import io

# Create a simple signature image
img = Image.new('RGBA', (300, 100), (255, 255, 255, 255))
draw = ImageDraw.Draw(img)

# Draw a simple signature
draw.line((20, 50, 80, 30), fill='black', width=3)
draw.line((80, 30, 120, 70), fill='black', width=3)
draw.line((120, 70, 180, 20), fill='black', width=3)
draw.line((180, 20, 220, 80), fill='black', width=3)
draw.line((220, 80, 280, 40), fill='black', width=3)

# Save as PNG
img.save('test-signature.png')
print("Test signature image created: test-signature.png")

# Also create base64 version for testing
buffer = io.BytesIO()
img.save(buffer, format='PNG')
base64_str = base64.b64encode(buffer.getvalue()).decode()
print("Base64 signature data length:", len(base64_str))
print("Base64 signature preview:", base64_str[:50] + "...")