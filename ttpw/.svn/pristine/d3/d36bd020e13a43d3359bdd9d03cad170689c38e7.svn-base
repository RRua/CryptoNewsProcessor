#-*- encoding: utf-8 -*-
from __future__ import print_function
from __future__ import unicode_literals
import re
import pprint

# XML element/attributes names syntax (including xml namespace):
SGML_name = r"""(?:[a-z][-_.a-z0-9]*(?::[-_.a-z0-9]*)?)"""

# XML attribute in an element tag (allow non-quoted value for bad html...)
SGML_att = SGML_name + r"""\s*=\s*(?:(?:"[^>"]*")|(?:'[^>']*')|(?:[^\s]+))"""

# XML tags (as group, with parenthesis !!!).
SGML_tag = r"""
    (
        (?:<!-- .*? -->)                   # XML/SGML comment
            |                              # -- OR --
        (?:
        <[!?/]?""" + SGML_name + r"""      # Start of tag/directive
            (?:\s+""" +SGML_att + r""")*   # [attributes]
         \s*[/?]?>                         # End of tag/directive - maybe autoclosed
        )
    )"""
SGML_tag_re = re.compile(SGML_tag, re.IGNORECASE | re.VERBOSE | re.DOTALL)


s = """   <toto:Animal gate:gate_Id='80' Animal.name="Reptile"  >
Anaconda
</Animal> <toto />
</truc>
<blaise:chose truc:mach-in="essai" blob.ref:xxx="34"  bold=true  />Rien a faire"""


lst = SGML_tag_re.split(s)

for i,v in enumerate(lst):
    if i%2 == 0:
        print("Val:", repr(v))
    else:
        print("Tag:", repr(v))
        
# pprint.pprint(lst)
# print("Tags: ", end="")
# pprint.pprint(lst[1::2])
# print("Contenu: ", end="")
# pprint.pprint(lst[::2])


