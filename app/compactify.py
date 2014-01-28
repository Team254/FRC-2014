"""
Made by Vidur Maheshwari
"""
fileadd = ''
jshead = '<script type="text/javascript">'
jsfoot = '</script>'
cshead = '<style type="text/css">'
csfoot = '</style>'
bad_word = ['<script src', '<link']
oldfile = open(fileadd + 'index.html')
newfile = open(fileadd + 'inline.html', 'w')
for line in oldfile:
    if not any(bad_word in line for bad_word in bad_word):
        newfile.write(line)
    else:
        item = line
        if "<script " in item:
            if "<!--" in item:
                continue
            else:
                i = item
                print(i)
                if not "</script>" in item:
                    continue
                i = i.split("<script ")
                i = i[1].split('></script>')
                i = i[0].split('src="')
                i = i[1].split('"')
                i = i[0]
                i = '' + i
                f = open(i, 'r')
                read = f.read()
                read = jshead + read
                read = read + jsfoot
                newfile.write(read)
                f.close()
        elif "<link " in item:
            if "<!--" in item:
                continue
            else:
                i = item
                i = i.split("<link ")
                print(i)
                i = i[1].split(' rel="stylesheet" type="text/css">')
                i = i[0].split('href="')
                i = i[1].split('"')
                i = i[0]
                print(i)
                i = '' + i
                f = open(i, 'r')
                read = f.read()
                read = cshead + read
                read = read + csfoot
                print(read)
                newfile.write(read)
                f.close()
oldfile.close()
newfile.close()
