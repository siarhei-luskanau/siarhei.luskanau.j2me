package akme.mobile.util;

import java.util.Vector;

public abstract class StringUtil {

	public static final String EMPTY_STRING = "";

    public static final int MAX_LONG_DIGITS = String.valueOf(Long.MAX_VALUE).length();
	
	/** Check if a string is null or zero-length. */
	public static boolean isEmpty(final String value) {
		return value == null || value.length() == 0;
	}

	/** Check if a string is null or zero-length or all whitespace. */
	public static boolean isBlank(final String value) {
		return value == null || value.length() == 0 || value.trim().length() == 0;
	}

	/** Substring method that returns null if given null. */
	public static String substring(final String str, final int beginIndex) {
		if (str == null) return null;
		else {
			return str.substring(beginIndex);
		}
	}

	/** Substring method that returns null if given null and will not use endIndex if it's beyond the string length. */
	public static String substring(final String str, final int beginIndex, final int endIndex) {
		if (str == null) return null;
		else if (endIndex <= str.length()){
			return str.substring(beginIndex,endIndex);
		} else {
			return str.substring(beginIndex);
		}
	}

	/** Append newVal to the baseStr if the newVal is not empty (null or zero-length). */
	public static void appendNotEmpty(StringBuffer sb, String newVal) {
		if (!isEmpty(newVal)) {
			sb.append(newVal);
		}
	}

	/** Append newVal to the baseStr if the newVal is not empty (null or zero-length) using a delimiter. */
	public static void appendNotEmpty(StringBuffer sb, String newVal, char delimit) {
		if (!isEmpty(newVal)) {
			if (sb.length() > 0) sb.append(delimit);
			sb.append(newVal);
		}
	}

	/**
	 * @return true if either both strings are null or if they are
	 * equal, false otherwise.
	 */
	public static boolean equal(final String value1, final String value2) {

	  if ( (value1 == null) && (value2 == null)) {
		return true;
	  }

	  if ( (value1 == null) || (value2 == null)) {
		return false;
	  }

	  return value1.equals(value2);
	}

	/** Count the occurances of a character in a string. */
	public static int count(final String str, final char chr) {
		return count(str, chr, 0);
	}

	/** Count the occurances of a character in a string from the start index. */
	public static int count(final String str, final char chr, final int start) {
		if (str == null || str.length() == 0) return 0;
		int n = 0;
		for (int i = str.indexOf(chr,start); i != -1; i = str.indexOf(chr,i+1)) {
			n++;
		}
		return n;
	}

	/** Count the occurances of a substring in a string. */
	public static int count(final String str, final String sub) {
		return count(str, sub, 0);
	}

	/** Count the occurances of a substring in a string. */
	public static int count(final String str, final String sub, final int start) {
		if (str == null || sub == null || str.length() == 0 || sub.length() == 0) return 0;
		int n = 0;
		for (int i = str.indexOf(sub,start); i != -1; i = str.indexOf(sub,i+sub.length())) {
			n++;
		}
		return n;
	}

    /** 
     * Pad an integer value with zeros to the given length (high-efficiency). 
     */
	public static String padZeroLeft(long value, int length) {
		return String.valueOf(padZeroLeftFast(value,length));
	}

    /** 
     * Pad an integer value with zeros to the given length (high-efficiency). 
     */
    public static char[] padZeroLeftFast(long value, int length) {
        int negSign = 0;
        if (value < 0) {
            negSign = 1;
            value = -value;
        }
        char[] result = new char[length + negSign];

        // 1. pad up to the given length
        for (int i = result.length - 1; i >= negSign; i--) {
            if (value > 0) {
                result[i] = (char)((long)value % 10 + '0');
                value /= 10;
            }
            else {
                result[i] = '0';
            }
        }

        // 2. if there are still digits remaining, prepend the extra digits
        if (value > 0) {
            char[] result2 = new char[MAX_LONG_DIGITS + negSign];
            int i = result2.length - length;
            System.arraycopy(result, negSign, result2, i, length);
            while (value > 0) {
                i--;
                result2[i] = (char)((long)value % 10 + '0');
                value /= 10;
            }
            int len2 = result2.length - i;
            result = new char[len2 + negSign];
            System.arraycopy(result2, i, result, negSign, len2);
        }
        if (negSign != 0)
            result[0] = '-';
        return result;
    }

	/** 
	 * Pad an integer value with zeros to the given length (high-efficiency).
	 * Use the given buffer at the given start position and return the length appended for convenience.
	 * e.g. <code>int pos = 0; pos += StringUtil.padZeroLeftFast(result,pos,cal.get(Calendar.YEAR),4);</code>
	 */
	public static int padZeroLeftFast(char[] result, int start, long value, int length) {
		int negSign = 0;
		if (value < 0) {
			negSign = 1;
			value = -value;
		}
		// pad up to the given length
		for (int i = length - 1; i >= negSign; i--) {
			if (value > 0) {
				result[start+i] = (char)((long)value % 10 + '0');
				value /= 10;
			}
			else {
				result[start+i] = '0';
			}
		}
		return length;
	}

	/** 
	 * Pad an integer value with zeros to the given length, appending to the given StringBuffer (medium-efficiency). 
	 * This is actually slightly slower than returning a char[] to be appended to a StringBuffer.
	 */
	public static void padZeroLeftAppend(StringBuffer result, long value, int length) {
		int negSign = 0;
		if (value < 0) {
			negSign = 1;
			value = -value;
		}
		int resultLength = length + negSign;
		int resultStart = result.length() + negSign;
		result.setLength( resultStart + resultLength );
		
		// 1. pad up to the given length
		for (int i = resultLength - 1; i >= negSign; i--) {
			if (value > 0) {
				result.setCharAt(resultStart + i, (char)((long)value % 10 + '0'));
				value /= 10;
			}
			else {
				result.setCharAt(resultStart + i, '0');
			}
		}

		// 2. if there are still digits remaining, prepend the extra digits
		if (value > 0) {
			char[] result2 = new char[MAX_LONG_DIGITS - length + negSign];
			int i = result2.length;
			while (value > 0) {
				i--;
				result2[i] = (char)((long)value % 10 + '0');
				value /= 10;
			}
			int len2 = result2.length - i;
			char[] result3 = new char[len2];
			System.arraycopy(result2, i, result3, 0, len2);
			result.insert(resultStart,result3);
		}
		if (negSign != 0)
			result.setCharAt(resultStart - negSign, '-');
	}
    /**
     * Delete specified characters from the source string.
     *
     * @param str source string
     * @param stripChars characters to be deleted
     * @return string with characters deleted
     */
    public static String stripCharsInString(final String str, final String stripChars) {
		if (str == null) {
		  return str;
		}
		char[] result = str.toCharArray();
		int n = 0;
		for (int i=0; i<result.length; i++) {
			char c = result[i];
			if (stripChars.indexOf(c) == -1) {
				if (n != i) {
					result[n] = c;
				}
				n++;
			}
		}
		return String.valueOf(result, 0, n);
    }

    /**
     * Only keep certain characters in the source string.
     *
     * @param str source string
     * @param keepChars characters to be kept
     * @return string with characters deleted
     */
    public static String stripCharsNotInString(final String str, final String keepChars) {
		if (str == null) {
		  return str;
		}
		char[] result = str.toCharArray();
		int n = 0;
		for (int i=0; i<result.length; i++) {
			char c = result[i];
			if (keepChars.indexOf(c) != -1) {
				if (n != i) {
					result[n] = c;
				}
				n++;
			}
		}
		return String.valueOf(result, 0, n);
    }
    
    /**
     * Stamp/overwrite characters at the start of the string.
     *
     * @param str source string
     * @param stamp character to stamp from start of string
     * @param leaveAtEnd length to leave untouched from end of string
     */
    public static String stampCharStartUntil(final String str, final char stamp, final int leaveAtEnd) {
    	if (str == null) return null;
    	if (leaveAtEnd >= str.length()) return str;
 		char[] result = str.toCharArray();
 		for (int i=0; i<(result.length-leaveAtEnd); i++) result[i] = stamp;
    	return String.valueOf(result);
    }
	    
    public static char[] stampCharStartUntil(final char[] chars, final char stamp, final int leaveAtEnd) {
    	if (chars == null) return null;
    	if (leaveAtEnd >= chars.length) return chars;
 		for (int i=0; i<(chars.length-leaveAtEnd); i++) chars[i] = stamp;
    	return chars;
    }
	    
	/** 
	 * Returns the position in src of the first search string found.
	 * @see findFirst for even more efficiency.
	 */
	public static int indexOfOne(final String src, final int start, final String[] search) {
		int pos = 0;
		int min = src.length();
		for (int i=0; i<search.length; i++) {
			pos = src.indexOf(search[i],start);
			if (pos != -1 && pos < min) {
				min = pos;
			}
		}
		return min;
	} 
	
	public static int indexOf(final char c, final char[] target, final int targetOffset, final int targetCount) {
		if (target == null || targetOffset < 0 || targetOffset >= targetCount) return -1;
		for (int i = targetOffset; i < targetCount; i++) if (c != target[i]) return i;
		return -1;
	}

	/**
	 * Code shared by String and StringBuffer to do searches. The
	 * source is the character array being searched, and the target
	 * is the string being searched for.
	 *
	 * @param   source       the characters being searched.
	 * @param   sourceOffset offset of the source string.
	 * @param   sourceCount  count of the source string.
	 * @param   target       the characters being searched for.
	 * @param   targetOffset offset of the target string.
	 * @param   targetCount  count of the target string.
	 * @param   fromIndex    the index to begin searching from.
	 */
	public static int indexOf(char[] source, int sourceOffset, int sourceCount,
					   char[] target, int targetOffset, int targetCount,
					   int fromIndex) {
	if (fromIndex >= sourceCount) {
			return (targetCount == 0 ? sourceCount : -1);
	}
		if (fromIndex < 0) {
			fromIndex = 0;
		}
	if (targetCount == 0) {
		return fromIndex;
	}

		char first  = target[targetOffset];
		int i = sourceOffset + fromIndex;
		int max = sourceOffset + (sourceCount - targetCount);

	startSearchForFirstChar:
		while (true) {
		/* Look for first character. */
		while (i <= max && source[i] != first) {
		i++;
		}
		if (i > max) {
		return -1;
		}

		/* Found first character, now look at the rest of v2 */
		int j = i + 1;
		int end = j + targetCount - 1;
		int k = targetOffset + 1;
		while (j < end) {
		if (source[j++] != target[k++]) {
			i++;
			/* Look for str's first char again. */
			continue startSearchForFirstChar;
		}
		}
		return i - sourceOffset;	/* Found whole string. */
		}
	}

	public static int lastIndexOf(final char c, final char[] target, final int targetOffset, final int targetCount) {
		if (target == null || targetOffset < 0 || targetOffset >= targetCount) return -1;
		for (int i = targetCount; i >= targetOffset; i--) if (c != target[i]) return i;
		return -1;
	}
	
	/**
	 * Code shared by String and StringBuffer to do searches. The
	 * source is the character array being searched, and the target
	 * is the string being searched for.
	 *
	 * @param   source       the characters being searched.
	 * @param   sourceOffset offset of the source string.
	 * @param   sourceCount  count of the source string.
	 * @param   target       the characters being searched for.
	 * @param   targetOffset offset of the target string.
	 * @param   targetCount  count of the target string.
	 * @param   fromIndex    the index to begin searching from.
	 */
	public static int lastIndexOf(char[] source, int sourceOffset, int sourceCount,
						   char[] target, int targetOffset, int targetCount,
						   int fromIndex) {
		/*
	 * Check arguments; return immediately where possible. For
	 * consistency, don't check for null str.
	 */
		int rightIndex = sourceCount - targetCount;
	if (fromIndex < 0) {
		return -1;
	}
	if (fromIndex > rightIndex) {
		fromIndex = rightIndex;
	}
	/* Empty string always matches. */
	if (targetCount == 0) {
		return fromIndex;
	}

		int strLastIndex = targetOffset + targetCount - 1;
	char strLastChar = target[strLastIndex];
	int min = sourceOffset + targetCount - 1;
	int i = min + fromIndex;

	startSearchForLastChar:
	while (true) {
		while (i >= min && source[i] != strLastChar) {
		i--;
		}
		if (i < min) {
		return -1;
		}
		int j = i - 1;
		int start = j - (targetCount - 1);
		int k = strLastIndex - 1;

		while (j > start) {
			if (source[j--] != target[k--]) {
			i--;
			continue startSearchForLastChar;
		}
		}
		return start - sourceOffset + 1;
	}
	}
	
	/** 
	 * Returns a array of positions in src of search strings in the order they are found.
	 * This is built for efficiency, remembering where it found things and keeping object creation to a minimum.
	 * 
	 * @param src The string to be searched.
	 * @param search The array of search criteria.
	 * @return Array of positions in src of search strings in the order they are found. 
	 * @see findAll for even more efficiency.
	 */
	public static int[] indexOfAll(final String src, final String[] search) {
		IntList result = new IntList();
		int[] idxAry = new int[search.length];
		int len = src.length();
		int pos = 0;
		while (pos != -1) {
			int minIdx = -1;
			int minPos = len;
			for (int i=0; i<search.length; i++) {
				pos = idxAry[i];
				if (pos == -1) continue;
				pos = src.indexOf(search[i],pos);
				idxAry[i] = pos;
				if (pos != -1 && pos < minPos) {
					minPos = pos;
					minIdx = i;
				}
			}
			if (minIdx != -1) {
				result.add(minPos);
				pos = minPos + search[minIdx].length();
				if (pos >= len) pos = -1;
				idxAry[minIdx] = pos;
			} else {
				pos = -1;
			}
		}
		return result.toArray();
	} 
	    
	/** 
	 * Sets resultPosAndIndex [0] and [1] with the position in src and search array index of first search string found.
	 */
	public static void findOne(final int[] resultPosAndIndex, final String src, final int start, final String[] search) {
		int pos = 0;
		int min = src.length();
		int minIdx = -1;
		for (int i=0; i<search.length; i++) {
			pos = src.indexOf(search[i],start);
			if (pos != -1 && pos < min) {
				min = pos;
				minIdx = i;
			}
		}
		resultPosAndIndex[0] = minIdx;
		resultPosAndIndex[1] = min;
	}
	
	/** 
	 * Returns a list of int[2] posAndIndex with the position and search array index of the string that was found.
	 * This results are ordered by position found.
	 * This is built for efficiency, remembering where it found things and keeping object creation to a minimum.
	 * 
	 * @param src The string to be searched.
	 * @param search The array of search criteria.
	 * @return List of int[2] indexAndPos where posAndIndex[0] is the position within src and posAndIndex[1] is the index of the search string found. 
	 */
	public static Vector findAll(final String src, final String[] search) {
		Vector result = new Vector();
		int[] idxAry = new int[search.length];
		int len = src.length();
		int pos = 0;
		while (pos != -1) {
			int minIdx = -1;
			int minPos = len;
			for (int i=0; i<search.length; i++) {
				pos = idxAry[i];
				if (pos == -1) continue;
				pos = src.indexOf(search[i],pos);
				idxAry[i] = pos;
				if (pos != -1 && pos < minPos) {
					minPos = pos;
					minIdx = i;
				}
			}
			if (minIdx != -1) {
				result.addElement(new int[] {minPos,minIdx});
				pos = minPos + search[minIdx].length();
				if (pos >= len) pos = -1;
				idxAry[minIdx] = pos;
			} else {
				pos = -1;
			}
		}
		return result;
	}

	/**
	 * Search and replace one occurence within the text string.
	 *
     * @param text Source string to be changed.
     * @param search Search string to be found.
     * @param replace Replacement string for search string.
     * @return Resulting string.
	 */
	public static String replaceOne(final String text, final String search, final String replace) {
		int idx = (text != null) ? text.indexOf(search) : -1;
		if (idx != -1) {
			return text.substring(0,idx) + replace + text.substring(idx+search.length());
		} else {
			return text;
		}
	}

    /**
     * Search and replace all occurences of one string with another string.
     * This is optimized to produce only one intermediate working object (StringBufferFast).
     *
	 * For example:
	 * <code><pre>
	 *    replaceAll( "ABC #V# DEF #V#", "#V", "x" );
	 * </pre></code>
	 * leaves s with the value "ABC x DEF x".
	 *
     * @param str Source string to be changed.
     * @param search Search string to be found.
     * @param replace Replacement string for search string.
     * @return Resulting string.
     */
	public static String replaceAll(final String str, final String search, final String replace) {
		int start = 0;
		int end = (str != null) ? str.indexOf(search) : -1;
		if (end == -1) return str;
		StringBuffer buf = new StringBuffer(str.length());
		while (end != -1) {
			buf.append(str.substring(start, end));
			buf.append(replace);
			start = end + search.length();
			end = str.indexOf(search, start);
		}
		if (start != str.length()) {
			buf.append(str.substring(start));
		}
		return buf.toString();
	}

	/**
	 * Replace special characters in the given String value.
	 * This will only create intermediary objects if necessary.
	 *
	 * @param value Value with characters to be replaced.
	 * @param search Characters to match.
	 * @param replace Replacements for the associated search characters.
	 * @return String with replacements.
	 */
	public static String replaceAll(final String value, final char[] search, final String[] replace) {
		if (value == null || value.length() == 0) return value;
		StringBuffer sb = null;
		for (int i = 0; i < value.length(); i++) {
			char c = value.charAt(i);
			int j = 0;
			do {
				if (c == search[j]) {
					if (sb == null) {
						sb = new StringBuffer(value.length());
						sb.append(value.substring(0,i));
					}
					sb.append(replace[j]);
					break; // break the loop
				}
				j++;
			} while (j < search.length);
			if (!(j < search.length) && sb != null) {
				// the character was not replaced and the result has been initialized
				sb.append(c);
			}
		}
		return (sb != null) ? sb.toString() : value;
	}
	
	public static void replaceAll(final StringBuffer result, final String value, final char[] search, final String[] replace) {
		if (value == null || value.length() == 0) return;
		StringBuffer sb = null;
		for (int i = 0; i < value.length(); i++) {
			char c = value.charAt(i);
			int j = 0;
			do {
				if (c == search[j]) {
					if (sb == null) {
						sb = result;
						sb.append(value.substring(0,i));
					}
					sb.append(replace[j]);
					break; // break the loop
				}
				j++;
			} while (j < search.length);
			if (!(j < search.length) && sb != null) {
				// the character was not replaced and the result has been initialized
				sb.append(c);
			}
		}
		if (sb == null) result.append(value);
	}

	/**
	 * Replace search strings in the given string value with replacement strings.
	 * This will only create intermediary objects if necessary and uses an efficient algorithm.
	 * 
	 * The algorithm works as follows: 
	 * (a) search for each search string, remembering their found positions;
	 * (b) replace the search string closest to the current position with its replacement;
	 * (c) increase the current position to the next closest found position;
	 * (d) repeat until no search strings are found from the current position.
	 * 
	 * This algorithm is 2x to 3x faster, or more, than doing multiple searches at one character increments,
	 * based on typical use of a long (dozens to hundres of chars) source string with a few short (word) replacements.
	 *
	 * @param value Value that may have search strings to be replaced.
	 * @param search Strings to find.
	 * @param replace Replacements for the associated search strings.
	 * @return String with replacements.
	 */
	public static String replaceAll(final String value, final String[] search, final String[] replace) {
		if (isEmpty(value) || search == null || replace == null) return value;
		final int oldlen = value.length();
		StringBuffer sb = null;
		int[] pos = new int[search.length];
		for (int j=0; j<search.length; j++) pos[j] = -1;
		for (int pos1 = 0, pos2 = 0; pos1 < oldlen; pos1 = pos2) {
			int repidx = 0;
			boolean found = false;
			for (int j=0; j<search.length; j++) {
				pos2 = pos[j];
				if (pos2 == oldlen) continue;
				if (pos2 == -1 || pos2 < pos1) {
					pos2 = value.indexOf(search[j],pos1);
					if (pos2 == -1) pos2 = oldlen;
					pos[j] = pos2;
				}
				if (pos2 != oldlen) {
					found = true;
					if (pos2 < pos[repidx]) repidx = j;
				}
			}
			if (found) {
				if (sb == null)	sb = new StringBuffer(oldlen);
				pos2 = pos[repidx];
				sb.append(value.substring(pos1,pos2));
				sb.append(replace[repidx]);
				pos2 += search[repidx].length();
			} else {
				if (sb != null) sb.append(value.substring(pos1,oldlen));
				pos2 = oldlen; 
			}
		}
		return (sb != null) ? sb.toString() : value;
	}
	
	public static String replaceAll(final String value, final String[] search, final char[] replace) {
		if (isEmpty(value) || search == null || replace == null) return value;
		final int oldlen = value.length();
		StringBuffer sb = null;
		int[] pos = new int[search.length];
		for (int j=0; j<search.length; j++) pos[j] = -1;
		for (int pos1 = 0, pos2 = 0; pos1 < oldlen; pos1 = pos2) {
			int repidx = 0;
			boolean found = false;
			for (int j=0; j<search.length; j++) {
				pos2 = pos[j];
				if (pos2 == oldlen) continue;
				if (pos2 == -1 || pos2 < pos1) {
					pos2 = value.indexOf(search[j],pos1);
					if (pos2 == -1) pos2 = oldlen;
					pos[j] = pos2;
				}
				if (pos2 != oldlen) {
					found = true;
					if (pos2 < pos[repidx]) repidx = j;
				}
			}
			if (found) {
				if (sb == null)	sb = new StringBuffer(oldlen);
				pos2 = pos[repidx];
				sb.append(value.substring(pos1,pos2));
				sb.append(replace[repidx]);
				pos2 += search[repidx].length();
			} else {
				if (sb != null) sb.append(value.substring(pos1,oldlen));
				pos2 = oldlen; 
			}
		}
		return (sb != null) ? sb.toString() : value;
	}
	
}
